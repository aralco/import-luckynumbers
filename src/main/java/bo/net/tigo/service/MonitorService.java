package bo.net.tigo.service;

import bo.net.tigo.dao.InAuditDao;
import bo.net.tigo.dao.JobDao;
import bo.net.tigo.dao.OutAuditDao;
import bo.net.tigo.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 10/22/14.
 */
@Service
public class MonitorService {
    @Autowired
    private JobDao jobDao;
    @Autowired
    private InAuditDao inAuditDao;
    @Autowired
    private OutAuditDao outAuditDao;

    private static final Logger logger = LoggerFactory.getLogger(MonitorService.class);

    @Transactional
    public List<Job> monitorJobs(Long jobId, String owner, String jobState, Date from, Date to)  {
        List<Job> jobs= new ArrayList<Job>(0);

        if(jobId!=0)  {
            jobs.add(jobDao.findOne(jobId));
        } else if(!owner.equals("NA"))  {
            jobs = jobDao.finByOwner(owner);
        } else if(!jobState.equals("NA"))   {
            jobs = jobDao.finByState(jobState);
        } else if(from!=null && to!=null)   {
            jobs = jobDao.finBetweenDates(from, to);
        } else {
            jobs = jobDao.findAll();
        }
        return jobs;
    }

    @Transactional
    public List<String> getInFile(Long taskId)   {
        return inAuditDao.findRowsByTask(taskId);
    }

    @Transactional
    public List<String> getOutFile(Long taskId)   {
        return outAuditDao.findRowsByTask(taskId);
    }
}
