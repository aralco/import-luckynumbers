package bo.net.tigo.service;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by aralco on 2/23/15.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class OutProcessorJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(OutProcessorJob.class);

    @Autowired
    private OutProcessor outProcessor;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Calling processOutFile");
        outProcessor.processOutFile();

    }
}
