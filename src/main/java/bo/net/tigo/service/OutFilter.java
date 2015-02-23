package bo.net.tigo.service;

import bo.net.tigo.dao.InAuditDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.exception.OutFileProcessorException;
import bo.net.tigo.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

/**
 * Created by aralco on 2/23/15.
 */
@Service
@Transactional
public class OutFilter {
    private static final Logger logger = LoggerFactory.getLogger(OutFilter.class);
    private static final String UTF_8 = "UTF-8";

    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private TaskDao taskDao;

    public void filterOutFile(File file) {
        logger.info("Filtering out File: " + file);
        String fileFullName = file.toString();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(UTF_8)));
            String line;
            String fileName = file.getName();
            logger.info("Filename to Filter:"+fileName);
            boolean goodFileToFilter = Pattern.matches("[0-9]{8}_[0-9]{6}\\.out", fileName);
            if(!goodFileToFilter)    {
                throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"El nombre del archivo:"+fileName+"  a ser procesado no es válido.");
            }

            String shortFilename = fileName.substring(0,15);
            Task task = taskDao.findByFileName(shortFilename);
            if(task==null)  {
                throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La tarea relacionada con el archivo:"+fileName+" no pudo ser encontrada o la tarea aún no existe.");
            } else if(task.getUrlout()!=null) {
                throw new LuckyNumbersGenericException(HttpStatus.CONFLICT.toString(),"La tarea relacionada con el archivo:"+fileName+ " ya fue procesada. Id de Tarea="+task.getId());
            }
            int linesInFile = 0;
            while (bufferedReader.readLine() != null) {
                linesInFile++;
            }
            Long rowsIn = inAuditDao.countInRowsByTask(task.getId());
            if(rowsIn!=linesInFile)    {
                logger.warn("Does file exists?:"+file+", fullname:"+fileFullName);
                if(file.delete())   {
                    logger.info("File has been deleted:"+file);
                    throw new OutFileProcessorException(HttpStatus.PARTIAL_CONTENT.toString(),"File:"+fileName+" is not ready to be processed.");
                }
            } else  {
                logger.info("Out file: "+fileName+", is valid and ready to process by task:"+task.getId());
            }

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
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }while(!newFile.exists());
            filterOutFile(newFile);

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
