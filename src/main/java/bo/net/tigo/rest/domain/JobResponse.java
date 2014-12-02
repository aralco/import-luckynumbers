package bo.net.tigo.rest.domain;

import bo.net.tigo.model.Job;

import java.util.List;

/**
 * Created by aralco on 11/10/14.
 */
public class JobResponse {
    private List<Job> jobs;

    public JobResponse(List<Job> jobs) {
        this.jobs = jobs;
    }

    public JobResponse() {
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }
}
