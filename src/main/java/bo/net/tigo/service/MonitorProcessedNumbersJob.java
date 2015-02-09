package bo.net.tigo.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by aralco on 2/8/15.
 */
public class MonitorProcessedNumbersJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(MonitorProcessedNumbersJob.class);
    @Autowired
    private FindAndSaveLuckyNumbers findAndSaveLuckyNumbers;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Calling processStartedPhase2Tasks");
        findAndSaveLuckyNumbers.processStartedPhase2Tasks();
    }
}
