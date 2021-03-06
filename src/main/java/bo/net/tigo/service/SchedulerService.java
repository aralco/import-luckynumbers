package bo.net.tigo.service;

import bo.net.tigo.dao.JobDao;
import bo.net.tigo.dao.TaskDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.model.*;
import bo.net.tigo.rest.domain.JobRequest;
import bo.net.tigo.rest.domain.TaskRequest;
import bo.net.tigo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
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
    @Autowired
    private AuditService auditService;

    private static final Logger logger = LoggerFactory.getLogger(SchedulerService.class);

    @Transactional
    public Job createJob(JobRequest jobRequest) {
        logger.info("createJob:"+jobRequest);
        auditService.audit(Action.CREAR_PROGRAMACION);
        Date currentDate = Calendar.getInstance().getTime();
        Job job = new Job();
        job.setName(jobRequest.getName());
        job.setDescription(jobRequest.getDescription());
        if(jobRequest.getNow()) {
            job.setScheduledDate(currentDate);
            if(jobRequest.getTasks().isEmpty())
                throw new LuckyNumbersGenericException(HttpStatus.BAD_REQUEST.toString(),"La programación debe tener por lo menos una tarea registrada.");
        }
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
        job.setCreatedDate(currentDate);
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
            task.setCreatedDate(currentDate);
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
        auditService.audit(Action.EDITAR_PROGRAMACION);
        if(!job.getState().equals(State.NOT_STARTED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"La programación relacionada debe tener estado No iniciado.");
        Date currentDate = Calendar.getInstance().getTime();
        if(job.getNow())
            job.setScheduledDate(currentDate);
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
        auditService.audit(Action.ELIMINAR_PROGRAMACION);
        Job job = jobDao.findOne(jobId);
        if(job==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La programación relacionada no pudo ser encontrada.");
        }
        if(!job.getState().equals(State.NOT_STARTED.name()))   {
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"La programación relacionada debe tener estado No iniciado.");
        }
        jobDao.delete(job);
    }

    @Transactional
    public Task createTask(Long jobId, TaskRequest taskRequest) {
        auditService.audit(Action.CREAR_TAREA);
        Job job = jobDao.findOne(jobId);
        if(job==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La programación relacionada no pudo ser encontrada.");
        }
        if(!job.getState().equals(State.NOT_STARTED.name()))   {
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"La programación relacionada debe tener estado No iniciado.");
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
        auditService.audit(Action.EDITAR_TAREA);
        Task persistedTask = taskDao.findOne(task.getId());
        if(persistedTask==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La tarea relacionada no pudo ser encontrada.");
        }
        if(!persistedTask.getStatus().equals(Status.SCHEDULED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"La programación relacionada debe tener estado No iniciado.");
        Date currentDate = Calendar.getInstance().getTime();
        persistedTask.setType(task.getType());
        persistedTask.setCity(task.getCity());
        persistedTask.setFrom(task.getFrom());
        persistedTask.setTo(task.getTo());
        persistedTask.setLastUpdate(currentDate);
        taskDao.update(persistedTask);
        return persistedTask;
    }

    @Transactional
    public void deleteTask(Long taskId) {
        auditService.audit(Action.ELIMINAR_TAREA);
        Task task = taskDao.findOne(taskId);
        if(task==null)
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La tarea relacionada no pudo ser encontrada.");
        if(!task.getStatus().equals(Status.SCHEDULED.name()))
            throw new LuckyNumbersGenericException(HttpStatus.PRECONDITION_FAILED.toString(),"La programación relacionada debe tener estado No iniciado.");
        Job job = task.getJob();
        job.setTotalTasks(job.getTotalTasks()-1);
        job.getTasks().remove(task);
        taskDao.delete(task);
    }

}
