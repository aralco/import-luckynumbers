package bo.net.tigo.service;

import bo.net.tigo.dao.JobDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.model.Job;
import bo.net.tigo.model.State;
import bo.net.tigo.model.Status;
import bo.net.tigo.model.Task;
import bo.net.tigo.rest.domain.JobRequest;
import bo.net.tigo.rest.domain.TaskRequest;
import bo.net.tigo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aralco on 10/22/14.
 */
@Service
public class SchedulerService {
    @Autowired
    private JobDao jobDao;
    @Autowired
    private TaskDao taskDao;

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Transactional
    public Job createJob(JobRequest jobRequest) {
        logger.info("createJob:"+jobRequest);
        Date creationDate = new Date();
        Job job = new Job();
        job.setName(jobRequest.getName());
        job.setDescription(jobRequest.getDescription());
        if(jobRequest.getNow())
            job.setScheduledDate(creationDate);
        else
            job.setScheduledDate(jobRequest.getScheduledDate());
        job.setNow(jobRequest.getNow());
        job.setState(State.NOT_STARTED.name());
        job.setOwner(SecurityUtils.getCurrentUsername());
        int totalTasks=0;
        if(jobRequest.getTasks()!=null && jobRequest.getTasks().size()>0)
            totalTasks=jobRequest.getTasks().size();
        job.setTotalTasks(totalTasks);
        job.setPassedTasks(0);
        job.setFailedTasks(0);
        job.setTotalCoverage("0%");
        job.setSummary("");
        job.setCreatedDate(creationDate);
        jobDao.save(job);

        Set<Task> tasks = new HashSet<Task>(0);
        for(TaskRequest taskRequest : jobRequest.getTasks())   {
            Task task = new Task();
            task.setType(taskRequest.getType());
            task.setCity(taskRequest.getCity());
            task.setFrom(taskRequest.getFrom());
            task.setTo(taskRequest.getTo());
            task.setExecutionDate(job.getScheduledDate());
            task.setStatus(Status.SCHEDULED.name());
            task.setProcessed(0);
            task.setPassed(0);
            task.setFailed(0);
            task.setJob(job);
            task.setSummary("");
            task.setCoverage("0%");
            task.setCreatedDate(creationDate);
            taskDao.save(task);
            tasks.add(task);
        }
        job.setTasks(tasks);
        return job;
    }

    @Transactional
    public Job getJob(Long jobId)   {
        return jobDao.findOne(jobId);
    }

    @Transactional
    public Job updateJob(Long jobId, Job job)   {
        logger.info("updateJob:job="+job);
        if(!job.getState().equals(State.NOT_STARTED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"Related Job must have NOT_STARTED state");
        Date currentDate = new Date();
        if(job.getNow())
            job.setScheduledDate(currentDate);
//        else
//            job.setScheduledDate(job.getScheduledDate());
        for(Task task : job.getTasks()) {
            task.setExecutionDate(job.getScheduledDate());
            task.setLastUpdate(currentDate);
        }
        job.setTotalTasks(job.getTasks().size());
        job.setLastUpdate(currentDate);
        jobDao.update(job);
        return job;
    }

    @Transactional
    public void deleteJob(Long jobId)   {
        Job job = jobDao.findOne(jobId);
        if(job==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"Related Job cannot be found");
        }
        if(!job.getState().equals(State.NOT_STARTED.name()))   {
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"Related Job must have NOT_STARTED state");
        }
        jobDao.delete(job);
    }

    @Transactional
    public Task createTask(Long jobId, TaskRequest taskRequest) {
        Job job = jobDao.findOne(jobId);
        if(job==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"Related Job cannot be found");
        }
        if(!job.getState().equals(State.NOT_STARTED.name()))   {
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"Related Job must have NOT_STARTED state");
        }
        Task task = new Task();
        task.setType(taskRequest.getType());
        task.setCity(taskRequest.getCity());
        task.setFrom(taskRequest.getFrom());
        task.setTo(taskRequest.getTo());
        task.setExecutionDate(job.getScheduledDate());
        task.setStatus(Status.SCHEDULED.name());
        task.setProcessed(0);
        task.setPassed(0);
        task.setFailed(0);
        task.setJob(job);
        task.setSummary("");
        task.setCoverage("0%");
        task.setCreatedDate(new Date());
        taskDao.save(task);
        job.setTotalTasks(job.getTotalTasks()+1);
        return task;
    }

    @Transactional
    public Task getTask(Long taskId) {
        return taskDao.findOne(taskId);
    }

    @Transactional
    public Task updateTask(Task task) {
        if(!task.getStatus().equals(Status.SCHEDULED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"Related Task must have NOT_SCHEDULED status");
        Date currentDate = new Date();
        task.setLastUpdate(currentDate);
        taskDao.update(task);
        Job job = task.getJob();
        job.setTotalTasks(job.getTotalTasks()+1);
        return task;
    }

    @Transactional
    public void deleteTask(Long taskId) {
        Task task = taskDao.findOne(taskId);
        if(task==null)
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"Related Task cannot be found");
        if(!task.getStatus().equals(Status.SCHEDULED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"Related Task must have NOT_SCHEDULED status");
        taskDao.delete(task);
        Job job = task.getJob();
        job.setTotalTasks(job.getTotalTasks()-1);
    }

}
