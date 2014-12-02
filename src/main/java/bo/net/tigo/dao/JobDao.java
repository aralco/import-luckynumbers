package bo.net.tigo.dao;

import bo.net.tigo.model.Job;

import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface JobDao {
    public void save(Job job);
    public void update(Job job);
    public Job findOne(Long jobId);
    public void delete(Job job);
    public List<Job> finByOwner(String owner);
    public List<Job> finByState(String state);
    public List<Job> finBetweenDates(Date from, Date to);
    public List<Job> findAll();
}
