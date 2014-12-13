package bo.net.tigo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by aralco on 11/18/14.
 */
public class InAudit {
    private Long id;
    private String row;
    private String number;
    private Integer city;
    private Integer channel;
    private String fileName;
    private Long taskId;
    private Long jobId;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date lastUpdate;

    public InAudit(Long id, String row, String number, Integer city, Integer channel, String fileName, Long taskId, Long jobId, Date createdDate, Date lastUpdate) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.city = city;
        this.channel = channel;
        this.fileName = fileName;
        this.taskId = taskId;
        this.jobId = jobId;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
    }

    public InAudit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
