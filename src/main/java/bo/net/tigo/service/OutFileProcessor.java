package bo.net.tigo.service;

import bo.net.tigo.dao.InAuditDao;
import bo.net.tigo.dao.OutAuditDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.exception.OutFileProcessorException;
import bo.net.tigo.model.Job;
import bo.net.tigo.model.OutAudit;
import bo.net.tigo.model.Status;
import bo.net.tigo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by aralco on 11/20/14.
 */
@Service
@Transactional
public class OutFileProcessor {

    private static final Logger logger = LoggerFactory.getLogger(OutFileProcessor.class);
    private static final String UTF_8 = "UTF-8";

    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private OutAuditDao outAuditDao;
    @Autowired
    private TaskDao taskDao;

    public void processOutFile(File file) {
        StringBuilder taskLog = new StringBuilder();
        logger.info("Processing File: " + file);
        String fileFullName = file.toString();
        BufferedReader bufferedReader = null;
        String splitBy = ",";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(UTF_8)));
            String line;
            int processed=0;
            int passed=0;
            int failed=0;
            float percentage=75;
            Date currentDate = Calendar.getInstance().getTime();
            String fileName = file.getName();
            logger.info("Filename to Process:"+fileName);
            boolean goodFileToProcess = Pattern.matches("[0-9]{8}_[0-9]{6}\\.out", fileName);
            if(!goodFileToProcess)    {
                throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"El archivo:"+fileName+"  a ser procesado no es válido.");
            }

            String shortFilename = fileName.substring(0,15);
            logger.info("array Size:"+shortFilename.length()+",values:"+shortFilename);
            Task task = taskDao.findByFileName(shortFilename);
            if(task==null)  {
                throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La tarea relacionada con el archivo:"+fileName+" no pudo ser encontrada o la tarea aún no existe.");
            } else if(task.getUrlout()!=null) {
                throw new LuckyNumbersGenericException(HttpStatus.CONFLICT.toString(),"La tarea relacionada con el archivo:"+fileName+ " ya fue procesada. Id de Tarea="+task.getId());
            }
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

                logger.info("Does file exists:"+file+", fullname:"+fileFullName);
                if(file.delete())   {
                    logger.info("File has been deleted:"+file);
                    throw new OutFileProcessorException(HttpStatus.PARTIAL_CONTENT.toString(),"File:"+fileName+" is not ready to be processed.");
                }
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

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException for file:" + file + ", with errors: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("IOException for file:" + file + ", with errors: " + e.getMessage());
            e.printStackTrace();
        } catch (OutFileProcessorException e) {
            logger.warn("OutFileProcessorException -> error:" + e.getErrorCode() + ", message: " + e.getErrorMessage());
            File newFile = new File(fileFullName);
            do  {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }while(!newFile.exists());
            processOutFile(newFile);

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

}
