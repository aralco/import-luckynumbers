package bo.net.tigo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by aralco on 11/18/14.
 */
public class OutAudit {
    private Long id;
    private String row;
    private String number;
    private Boolean luckyReserved;
    private Integer codePassed;
    private String codeFailed;
    private String message;
    private String fileName;
    private Long taskId;
    private Long jobId;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date lastUpdate;

    public OutAudit(Long id, String row, String number, Boolean luckyReserved, Integer codePassed, String codeFailed, String message, String fileName, Long taskId, Long jobId, Date createdDate, Date lastUpdate) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.luckyReserved = luckyReserved;
        this.codePassed = codePassed;
        this.codeFailed = codeFailed;
        this.message = message;
        this.fileName = fileName;
        this.taskId = taskId;
        this.jobId = jobId;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
    }

    public OutAudit() {
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

    public Boolean getLuckyReserved() {
        return luckyReserved;
    }

    public void setLuckyReserved(Boolean luckyReserved) {
        this.luckyReserved = luckyReserved;
    }

    public Integer getCodePassed() {
        return codePassed;
    }

    public void setCodePassed(Integer codePassed) {
        this.codePassed = codePassed;
    }

    public String getCodeFailed() {
        return codeFailed;
    }

    public void setCodeFailed(String codeFailed) {
        this.codeFailed = codeFailed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
