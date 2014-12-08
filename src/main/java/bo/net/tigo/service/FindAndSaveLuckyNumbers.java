package bo.net.tigo.service;

import bo.net.tigo.dao.*;
import bo.net.tigo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/28/14.
 */
@Service
public class FindAndSaveLuckyNumbers {
    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private OutAuditDao outAuditDao;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private BCCSDao bccsDao;
    @Autowired
    private LuckyNumbersDao luckyNumbersDao;
    @Autowired
    private NotificationService notificationService;

    private static final Logger logger = LoggerFactory.getLogger(FindAndSaveLuckyNumbers.class);

    @Transactional
    public void processStartedPhase2Tasks() {
        List<Task> startedPhase2Tasks = taskDao.findByStatus(Status.STARTED_PHASE2);
        logger.info("Total startedPhase2Tasks Tasks:" + startedPhase2Tasks.size());
        if(startedPhase2Tasks.size()>=0)    {
            Calendar calendar = Calendar.getInstance();
            for(Task task:startedPhase2Tasks) {
                boolean taskCompletedOK=true;
                StringBuilder errorCause = new StringBuilder("Causa del error: ||");
                logger.info("Looking for lucky numbers on task:"+task.getId());
                long reservedLuckyNumbers=0;
                long rolledBackNumbers=0;
                long lockedNumbersInBccs=0;
                long reservedNumbersInBccs=0;
                long unlockedNumbers=0;
                float taskPercentage=100;
                StringBuilder taskLog = new StringBuilder();
                Date currentDate = calendar.getTime();
                Job job = task.getJob();
                List<OutAudit> outAuditList = outAuditDao.findByTask(task.getId());
                for(OutAudit outAudit : outAuditList )  {
                    if(outAudit.getCodePassed().equals(0)) {
                        logger.info("Lucky Number to be reserved, calling SP3_RERSERVANL_NROCUENTACOYLC:"+outAudit.getNumber());
                        String message = bccsDao.reserveNumber(outAudit.getNumber());
                        if(message!=null && message.contains("OK"))  {
                            logger.info("Lucky Number has been correctly reserved on BCCS:"+message);
                            outAudit.setLuckyReserved(true);
                            reservedLuckyNumbers++;
                        } else  {
                            logger.info("Lucky Number "+outAudit.getNumber()+" canNOT been reserved on BCCS, calling rollback CONCILI_RESERVE_LUCKY_BCCS:"+message);
                            taskLog.append("Lucky Number ").append(outAudit.getNumber()).append(" no pudo ser reservado en BCCS, haciendo Rollback.").append(" ||");
                            rolledBackNumbers++;
                            taskCompletedOK=false;
                            errorCause.append("Lucky Number ").append(outAudit.getNumber()).append(" no pudo ser reservado en BCCS. ||");
                            luckyNumbersDao.unReserveNumber(outAudit.getNumber(), false);
                            logger.info("Rollback completed for number:"+outAudit.getNumber());
                            taskLog.append("Rollback completado para el número: ").append(outAudit.getNumber()).append(" ||");
                        }
                    } else if(outAudit.getMessage().equals("Error al insertar el numero")){
                        logger.info("Number processed is not lucky:"+outAudit.getNumber());
                    } else {
                        logger.info("Number processed is not lucky and has errors:"+outAudit.getNumber());
                        taskCompletedOK=false;
                        errorCause.append("Se encontró registro con valores incorrectos: ").append(outAudit.getRow()).append(" ||");
                    }

                }
                taskLog.append("Total de Números Lucky reservados en BCCS: ").append(reservedLuckyNumbers).append(" ||");
                //CONCILIATION: Call BCCS to ask for numbers with state LN, call only if lucky numbers were reserved
                if(task.getPassed()>0 && reservedLuckyNumbers>0)  {
                    logger.info("Conciliation of LuckyNumbers: Calling BCCS:SP1_LNROSLNXSUCURSALNRODESDEHASTA to ask for LN numbers using values:city="+task.getCity()+",from="+task.getFrom()+",to="+task.getTo());
                    List<InAudit> reservedNumbers = bccsDao.getReservedNumbers(task.getCity(), task.getFrom(), task.getTo());
                    if(reservedNumbers!=null) {
                        if(reservedNumbers.size()!=reservedLuckyNumbers) {
                            taskCompletedOK = false;
                            errorCause.append("La cantidad de números con estado LN en BCCS es diferente a la cantidad de números lucky en Lucky_Number. ||");
                        }
                        reservedNumbersInBccs=reservedNumbers.size();
                        logger.info("Conciliation of LuckyNumbers: LuckyNumbers in BCCS: "+reservedNumbers.size()+" -VS- LuckyNumbers in LUCKY_NUMBERS:"+task.getPassed());
                        taskLog.append("Conciliación de LuckyNumbers: || LuckyNumbers en BCCS: ").append(reservedNumbers.size()).append(" || LuckyNumbers en LUCKY_NUMBERS:").append(task.getPassed()).append(" ||");
                    } else  {
                        taskCompletedOK=false;
                        logger.info("Conciliation of LuckyNumbers: LN Numbers in BCCS seems to be empty.");
                        taskLog.append("Conciliation of LuckyNumbers: LN Numbers in BCCS seems to be empty. ||");
                    }
                } else {
                    logger.info("No Lucky Numbers found on processed file");
                }

                //Call to BCCS to change state of numbers from LC(locked) to LI(free), call only for FREE numbers
                if(task.getType().equals(Type.FREE.name())) {
                    logger.info("Unlock free numbers on BCCS, calling :SP2_LNROSLCXSUCURSALNRODESDEHASTAPORC_ACTESTLI1 to unlock numbers using values:city="+task.getCity()+",from="+task.getFrom()+",to="+task.getTo());
                    List<InAudit> unlockedNumberList = bccsDao.unlockNumbers(task.getCity(),task.getFrom(),task.getTo());
                    if(unlockedNumberList!=null)   {
                        if(!(unlockedNumberList.size()>0))  {
                            taskCompletedOK=false;
                            errorCause.append("La cantidad de números que cambiaron a estado LI en BCCS es incorrecta. Cantidad: ").append(unlockedNumberList.size()).append(" ||");
                        }
                        unlockedNumbers=unlockedNumberList.size();
                        logger.info("Total unlockedNumbers on BCCS: "+unlockedNumberList.size());
                        taskLog.append("Total números desbloqueados en BCCS: ").append(unlockedNumberList.size()).append(" ||");
                    } else  {
                        taskCompletedOK=false;
                        logger.info("Total unlockedNumbers on BCCS seems to be empty.");
                        taskLog.append("Total unlockedNumbers on BCCS seems to be empty. ||");
                    }
                    //CONCILIATION: Call to BCCS to ask for LC(locked) numbers after unlocking, call only for FREE numbers
                    logger.info("Conciliation of locked numbers on BCCS: Calling BCCS:SP1_LNROSLCXSUCURSALNRODESDEHASTA to list unlocked numbers using values:city="+task.getCity()+",from="+task.getFrom()+",to="+task.getTo());
                    List<InAudit> lockedNumbers = bccsDao.getLockedNumbers(task.getCity(), task.getFrom(), task.getTo());
                    if(lockedNumbers!=null)  {
                        if(lockedNumbers.size()>0)  {
                            errorCause.append("La cantidad de números con estado LC en BCCS es incorrecta. Cantidad:").append(lockedNumbers.size()).append(" ||");
                            taskCompletedOK=false;
                        }
                        lockedNumbersInBccs=lockedNumbers.size();
                        logger.info("Conciliation for unlocked numbers with LC state in BCCS:"+lockedNumbers.size());
                        taskLog.append("Total de números bloqueados en BCCS:").append(lockedNumbers.size()).append(" ||");
                    }
                    else    {
                        taskCompletedOK=false;
                        logger.info("LC Numbers in BCCS seems to be empty.");
                        taskLog.append("LC Numbers in BCCS seems to be empty. ||");
                    }

                }

                if(taskCompletedOK) {
                    task.setStatus(Status.COMPLETED_OK.name());
                    taskLog.append("Tarea completada con éxito. ||");
                    task.setSummary(task.getSummary()+taskLog.toString());
                    job.setPassedTasks(job.getPassedTasks()+1);
                } else  {
                    task.setStatus(Status.COMPLETED_WITH_ERRORS.name());
                    taskLog.append("Tarea completada con errores. ||");
                    task.setSummary(task.getSummary()+taskLog.toString()+errorCause.toString());
                    job.setFailedTasks(job.getFailedTasks()+1);
                }
                task.setLnNumbersInBccs(reservedNumbersInBccs);
                task.setReservedLuckyNumbers(Long.valueOf(task.getPassed()));
                task.setRolledBackNumbers(rolledBackNumbers);
                task.setUnlockedNumbers(unlockedNumbers);
                task.setLcNumbersInBccs(lockedNumbersInBccs);

                task.setCoverage(taskPercentage+"%");
                task.setLastUpdate(currentDate);
                logger.info("Task completed with status: "+task.getStatus());

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
                    job.setTotalCoverage("90%");
                    logger.info("Job is not yet completed progress:" + job.getTotalCoverage());
                }

                Long inFiles = inAuditDao.countInFilesByJob(job.getId());
                Long outFiles = outAuditDao.countOutFilesByJob(job.getId());
                logger.info("Total created files: "+(inFiles==null?0:inFiles)+"(.in) -VS- "+(outFiles==null?0:outFiles)+"(.out)");

                job.setLnNumbersInBccs((job.getLnNumbersInBccs()==null?0:job.getLnNumbersInBccs())+reservedNumbersInBccs);
                job.setReservedLuckyNumbers((job.getReservedLuckyNumbers()==null?0:job.getReservedLuckyNumbers())+task.getPassed());
                job.setRolledBackNumbers((job.getRolledBackNumbers()==null?0:job.getRolledBackNumbers())+rolledBackNumbers);
                job.setUnlockedNumbers((job.getUnlockedNumbers()==null?0:job.getUnlockedNumbers())+unlockedNumbers);
                job.setLcNumbersInBccs((job.getLcNumbersInBccs()==null?0:job.getLcNumbersInBccs())+lockedNumbersInBccs);

                String jobSummary =
                        "Total de archivos .in creados: "+(inFiles==null?0:inFiles)+" ||"+
                        "Total de archivos .out creados: "+(outFiles==null?0:outFiles)+" ||"+
                        "--------------------------------------------------------------- ||"+
                        "Total de Números con estado LN en BCCS: "+job.getLnNumbersInBccs()+" ||"+
                        "Total de Números Lucky en Lucky_Number: "+job.getReservedLuckyNumbers()+" ||"+
                        "--------------------------------------------------------------- ||"+
                        "Total de Números con rollback: "+job.getRolledBackNumbers()+" ||"+
                        "--------------------------------------------------------------- ||"+
                        "Total de Números desbloqueados en BCCS: "+job.getUnlockedNumbers()+" ||"+
                        "--------------------------------------------------------------- ||"+
                        "Total de Números con estado LC en BCCS: "+job.getLcNumbersInBccs()+" ||";

                job.setSummary(jobSummary);
                job.setLastUpdate(currentDate);
                logger.info("Job remains with state: "+job.getState());
            }


        } else  {
            logger.info("No startedPhase2Tasks tasks to execute:" + startedPhase2Tasks.size());
        }

    }
}
