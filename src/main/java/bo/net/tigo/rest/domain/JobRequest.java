package bo.net.tigo.rest.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

/**
 * Created by aralco on 11/10/14.
 */
public class JobRequest {
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @JsonSerialize(using=DateSerializer.class)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull
    private Date scheduledDate; //yyyy-MM-ddThh:mm:ss±hh:mm; e.g. 2014-11-05T12:54:00-04:00
    @NotNull
    private Boolean now;  //If user decides to start now. false by default.
    @NotNull
    private Set<TaskRequest> tasks;

    public JobRequest(String name, String description, Date scheduledDate, Boolean now) {
        this.name = name;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.now = now;
    }

    public JobRequest(String name, String description, Date scheduledDate, Boolean now, Set<TaskRequest> tasks) {
        this.name = name;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.now = now;
        this.tasks = tasks;
    }

    public JobRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Boolean getNow() {
        return now;
    }

    public void setNow(Boolean now) {
        this.now = now;
    }

    public Set<TaskRequest> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskRequest> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "JobRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", now=" + now +
                ", tasks=" + tasks +
                '}';
    }
}
