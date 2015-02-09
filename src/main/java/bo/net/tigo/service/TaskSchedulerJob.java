package bo.net.tigo.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.IOException;

/**
 * Created by aralco on 2/8/15.
 */
public class TaskSchedulerJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerJob.class);
    @Autowired
    private GetFrozenAndFreeNumbers getFrozenAndFreeNumbers;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Calling processScheduledTasks");
        try {
            getFrozenAndFreeNumbers.processScheduledTasks();
        } catch (IOException e)   {
            logger.warn("IO Exception when creating .in file.");
        }

    }
}
