package bo.net.tigo.dao;

import bo.net.tigo.model.Job;
import bo.net.tigo.model.Status;
import bo.net.tigo.model.Task;

import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface TaskDao {
    public void save(Task task);

    public void update(Task task);

    public Task findOne(Long taskId);

    public void delete(Task task);

    public List<Task> findAll();

    public List<Task> findByJob(Job job);

    public List<Task> findScheduledAndReScheduledTasks(Date currentDate);

    public List<Task> findbyStatus(Status status);

    public Task findByFileName(String filename);
}
