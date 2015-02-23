package bo.net.tigo.service;

import bo.net.tigo.dao.InAuditDao;
import bo.net.tigo.dao.OutAuditDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.model.Job;
import bo.net.tigo.model.OutAudit;
import bo.net.tigo.model.Status;
import bo.net.tigo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 2/23/15.
 */
@Service
public class OutProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OutProcessor.class);

    private static final String UTF_8 = "UTF-8";

    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private OutAuditDao outAuditDao;
    @Autowired
    private TaskDao taskDao;
    @Value("${file.path.import.out}")
    private String filePath;

    @Transactional
    public void processOutFile()    {
        List<Task> tasksReadyToProcess = taskDao.findByStatus(Status.COMPLETED_PHASE1_OK);
        logger.info("Total tasksReadyToProcess Tasks:" + tasksReadyToProcess.size());
        if(tasksReadyToProcess.size() > 0)  {
            String splitBy = ",";
            for(Task task : tasksReadyToProcess)    {
                StringBuilder taskLog = new StringBuilder();
                BufferedReader bufferedReader = null;
                File file = null;
                String fileName = task.getUrlin().replace("in","out");
                try {
                    file = new File(filePath+File.separator+fileName);
                    if(file.exists())   {
                        logger.info("Validating out File: " + fileName+", for task:"+task.getId());
                        if(task.getUrlout()!=null) {
                            throw new LuckyNumbersGenericException(HttpStatus.CONFLICT.toString(),"La tarea relacionada con el archivo:"+fileName+ " ya fue procesada. Id de Tarea="+task.getId());
                        }
                        bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(UTF_8)));
                        String line;
                        int processed=0;
                        int passed=0;
                        int failed=0;
                        int percentage=75;
                        Date currentDate = Calendar.getInstance().getTime();

                        List<OutAudit> outAuditList = new ArrayList<OutAudit>(0);
                        while ((line = bufferedReader.readLine()) != null) {
                            // use comma as separator
                            logger.info("line to process:"+line);
                            String[] outRow = line.split(splitBy);
                            OutAudit outAudit = new OutAudit();
                            outAudit.setFileName(fileName);
                            outAudit.setRow(line);
                            outAudit.setNumber(outRow[0]);
                            outAudit.setCodePassed(Integer.valueOf(outRow[1]));
                            outAudit.setCodeFailed(outRow[2]);
                            outAudit.setMessage(outRow[3]);
                            outAudit.setLuckyReserved(false);
                            outAudit.setCreatedDate(currentDate);
                            outAudit.setTaskId(task.getId());
                            outAudit.setJobId(task.getJob().getId());
                            outAuditList.add(outAudit);
                            processed++;
                            if(outAudit.getCodePassed().equals(0)) {
                                passed++;
                            } else {
                                failed++;
                            }
                        }
                        Long rowsIn = inAuditDao.countInRowsByTask(task.getId());
                        if(rowsIn==outAuditList.size())    {
                            taskLog.append("********************************************************** ||");
                            taskLog.append("Procesando archivo .out: ").append(fileName).append("||");
                            for(OutAudit audit : outAuditList) {
                                outAuditDao.save(audit);
                            }
                        } else  {
                            throw new LuckyNumbersGenericException(HttpStatus.PARTIAL_CONTENT.toString(),"Out file:"+fileName+" for task: "+task.getId()+", is not ready to be processed.");
                        }

                        Job job = task.getJob();
                        Long inFiles = inAuditDao.countInFilesByJob(job.getId());
                        Long outFiles = outAuditDao.countOutFilesByJob(job.getId());

                        logger.info("Total created files: "+(inFiles==null?0:inFiles)+"(.in) -VS- "+(outFiles==null?0:outFiles)+"(.out)");
                        logger.info("Out file processing successfully completed.");
                        taskLog.append("Procesamiento del archivo .out completado exitosamente. ||");

                        String jobSummary =
                                "Total de archivos .in creados: "+(inFiles==null?0:inFiles)+" ||"+
                                        "Total de archivos .out creados: "+(outFiles==null?0:outFiles)+" ||";

                        job.setSummary(jobSummary);
                        job.setTotalCoverage(percentage+"%");
                        job.setLastUpdate(currentDate);

                        task.setUrlout(fileName);
                        task.setStatus(Status.STARTED_PHASE2.name());
                        task.setProcessed(processed);
                        task.setPassed(passed);
                        task.setFailed(failed);
                        task.setCoverage(percentage+"%");
                        task.setSummary(task.getSummary()+taskLog.toString());
                        task.setLastUpdate(currentDate);

                    } else  {
                        logger.warn("Out file: "+fileName+", for task: "+task.getId()+", doesn't exist yet.");
                    }

                } catch (FileNotFoundException e) {
                    logger.error("FileNotFoundException for file:" + file + ", with errors: " + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    logger.error("IOException for file:" + file + ", with errors: " + e.getMessage());
                    e.printStackTrace();
                } catch (LuckyNumbersGenericException e) {
                    logger.warn("LuckyNumbersGenericException -> error:" + e.getErrorCode() + ", message: " + e.getErrorMessage());
                } catch (Exception e)  {
                    logger.error("Unknown exception -> error:" + e.toString() + ", message: " + e.getMessage());
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            logger.error("IOException for file:"+file+", with errors: "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }


            }

        } else  {
            logger.info("No completedPhase1_OK tasks to process.");
        }
    }

}
