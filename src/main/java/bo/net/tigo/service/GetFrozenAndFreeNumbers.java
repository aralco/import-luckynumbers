package bo.net.tigo.service;

import bo.net.tigo.dao.BCCSDao;
import bo.net.tigo.dao.InAuditDao;
import bo.net.tigo.dao.OutAuditDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/11/14.
 */
@Service
public class GetFrozenAndFreeNumbers {

    private static final Logger logger = LoggerFactory.getLogger(GetFrozenAndFreeNumbers.class);
    private static final String UTF_8 = "UTF-8";
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private MessageChannel ftpChannelOUT;
    @Autowired
    private BCCSDao bccsDao;
    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private OutAuditDao outAuditDao;
    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void processScheduledTasks() throws IOException {
        List<Task> scheduledTasks = taskDao.findScheduledTasksInRange(new Date());
        logger.info("Total Scheduled Tasks:" + scheduledTasks.size());
        if(scheduledTasks.size()<=0)   {
            logger.info("No Scheduled tasks to execute:" + scheduledTasks.size());
        } else  {
            Calendar calendar = Calendar.getInstance();
            for(Task task:scheduledTasks) {
                StringBuilder taskLog = new StringBuilder();
                Date currentDate = calendar.getTime();
                boolean noStoredProcedureError=false;
                int jobPercentage=50;
                int taskPercentage;
                Job job = task.getJob();
                logger.info("Start task execution for task=> taskId:"+task.getId()+", status="+task.getStatus()+", executionDate="+task.getExecutionDate()+", job="+job);
                taskLog.append("Tarea inicializada.||");
                task.setStatus(Status.STARTED.name());
                task.setExecutionDate(currentDate);
                if(job.getState().equals(State.NOT_STARTED.name()))  {
                   job.setState(State.IN_PROGRESS.name());
                }
                logger.info("Start task execution for task=> taskId:" + task.getId() + ", status=" + task.getStatus() + ", executionDate=" + task.getExecutionDate() + ", job=" + task.getJob().getState());
                taskLog.append("Obteniendo números ");
                List<InAudit> retrievedNumbers = new ArrayList<InAudit>(0);
                try {
                    if(task.getType().equals(Type.FREE.name())) {
                        taskLog.append("LIBRES. ||");
                        retrievedNumbers = bccsDao.getFreeNumbers(task.getCity(), task.getFrom(), task.getTo());
                    } else if(task.getType().equals(Type.FROZEN.name()))  {
                        taskLog.append("CONGELADOS. ||");
                        retrievedNumbers = bccsDao.getFrozenNumbers(task.getCity(), task.getFrom(), task.getTo());
                    } else  {
                        throw new LuckyNumbersGenericException(HttpStatus.BAD_REQUEST.toString(),"Task doesn't have a valid type. FREE/FROZEN.");
                    }
                }catch (LuckyNumbersGenericException e)    {
                    logger.error(e.getMessage());
                    noStoredProcedureError=true;
                }catch (Exception e)    {
                    logger.error("Stored procedure to get free/frozen numbers doesn't exist or is not available.");
                    noStoredProcedureError=true;
                }
                logger.info("Total retrieved numbers:"+retrievedNumbers.toString());
                taskLog.append("Total de números obtenidos: ").append(retrievedNumbers.size()).append(" ||");
                if(retrievedNumbers.size()>0)   {
                    String fileContent =
                            "Numero,Ciudad,Channel";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String fileName = sdf.format(currentDate)+".in";

                    for(InAudit inAudit : retrievedNumbers) {
                        fileContent=fileContent+"\n"+inAudit.getRow();
                        inAudit.setCreatedDate(currentDate);
                        inAudit.setCity(task.getCity());
                        inAudit.setTaskId(task.getId());
                        inAudit.setJobId(task.getJob().getId());
                        String fields[] = inAudit.getRow().split(",");
                        inAudit.setNumber(fields[0]);
                        inAudit.setCity(Integer.valueOf(fields[1]));
                        inAudit.setChannel(Integer.valueOf(fields[2]));
                        inAudit.setFileName(fileName);
                        inAuditDao.save(inAudit);
                        logger.info("Processing retrievedNumber:"+inAudit.getRow());
                    }
                    logger.info("File generation:"+retrievedNumbers);


                    File inFile = new File(fileName);
                    logger.info("File generated:"+inFile);
                    taskLog.append("Archivo generado: ").append(fileName).append(" ||");

                    if(!inFile.exists())    {
                        if(inFile.createNewFile())
                            logger.info("File didn't exist and was successfully created:"+inFile);
                        else
                            logger.warn("File already exists:" + inFile);
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(inFile);
                    byte[] contentInBytes = fileContent.getBytes(Charset.forName(UTF_8));
                    fileOutputStream.write(contentInBytes);
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    final Message<File> fileMessage = MessageBuilder.withPayload(inFile).build();
                    ftpChannelOUT.send(fileMessage);
                    logger.info("Sending file :"+fileName+" to FTP server.");
                    taskLog.append("Enviando archivo ").append(fileName).append(" al servidor FTP. ||");
                    task.setStatus(Status.COMPLETED_PHASE1_OK.name());
                    logger.info("Phase 1 completed successfully.");
                    taskLog.append("Obtención de números completada exitosamente.").append("||");
                    task.setUrlin(fileName);
                    calendar.add(Calendar.SECOND, +1);
                    taskPercentage=50;
                } else  {
                    task.setStatus(Status.COMPLETED_WITH_ERRORS.name());
                    String errorCause = "";
                    if(noStoredProcedureError)  {
                        errorCause = "El procedimiento almacenado para obtener números libres y/o congelados no existe.";
                    } else  {
                        errorCause = "El archivo .in no pudo ser generado, debido a que no existen números a ser procesados.";
                        logger.warn("Phase 1 completed with errors. .in File could not be generated, because there are not numbers to be processed.");
                    }
                    taskLog.append("Tarea completada con errores. ||").append("Causa de error: ").append(errorCause).append(" ||");
                    task.setUrlin("NA");
                    task.setUrlout("NA");
                    task.setLnNumbersInBccs((long) 0);
                    task.setReservedLuckyNumbers((long) 0);
                    task.setRolledBackNumbers((long)0);
                    task.setUnlockedNumbers((long)0);
                    task.setLcNumbersInBccs((long)0);
                    task.setDiffReservedNumbers((long)0);
                    job.setFailedTasks(job.getFailedTasks()+1);
                    taskPercentage=100;
                }
                Long inFiles = inAuditDao.countInFilesByJob(job.getId());
                Long outFiles = outAuditDao.countOutFilesByJob(job.getId());
                logger.info("Total created files: "+(inFiles==null?0:inFiles)+"(.in) -VS- "+(outFiles==null?0:outFiles)+"(.out)");
                String jobSummary =
                        "Total de archivos .in creados: "+(inFiles==null?0:inFiles)+" ||"+
                        "Total de archivos .out creados: "+(outFiles==null?0:outFiles)+" ||";
                //job is completed correctly
                if(job.getTotalTasks().equals(job.getPassedTasks()))    {
                    job.setState(State.DONE.name());
                    job.setTotalCoverage(taskPercentage+"%");
                    notificationService.sendNotification(job.getOwner(), job.getName(), State.DONE);
                    //job is completed with errors
                } else if(job.getTotalTasks().equals(job.getPassedTasks()+job.getFailedTasks())) {
                    job.setState(State.CRITERIA_ACCEPTANCE.name());
                    job.setTotalCoverage(taskPercentage+"%");
                    notificationService.sendNotification(job.getOwner(), job.getName(), State.CRITERIA_ACCEPTANCE);
                } else  {
                    job.setTotalCoverage(jobPercentage+"%");
                    logger.info("Job is not yet completed progress:" + job.getTotalCoverage());
                }

                job.setSummary(jobSummary);
                job.setLastUpdate(currentDate);
                task.setCoverage(taskPercentage+"%");
                task.setSummary((task.getSummary()==null?"":task.getSummary())+taskLog.toString());
                task.setLastUpdate(currentDate);
            }
        }
    }
}
