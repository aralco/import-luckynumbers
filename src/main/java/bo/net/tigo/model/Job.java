package bo.net.tigo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import java.util.Date;
import java.util.Set;

/**
 * Created by aralco on 11/8/14.
 */
public class Job {
    private Long id;
    private String name;
    private String description;
    @JsonSerialize(using=DateSerializer.class)
//    @DateTimeFormat(pattern = "yyyy-MM-ddThh:mm:ss±hh:mm")
    private Date scheduledDate; //yyyy-MM-ddThh:mm:ss±hh:mm; e.g. 2014-11-05T12:54:00-04:00
    private Boolean now;  //If user decides to start now. false by default.
    private String state; //{NOT_STARTED;IN_PROGRESS;DONE};
    private String owner;
    private Integer totalTasks;
    private Integer passedTasks;	//total quantity of tasks that run without errors
    private Integer failedTasks;    //total quantity of tasks that run with errors
    private String totalCoverage; //{0%-100%}
    @JsonIgnore
    private Long lnNumbersInBccs;
    @JsonIgnore
    private Long reservedLuckyNumbers;
    @JsonIgnore
    private Long rolledBackNumbers;
    @JsonIgnore
    private Long unlockedNumbers;
    @JsonIgnore
    private Long lcNumbersInBccs;
    @JsonIgnore
    private Long diffReservedNumbers;
    @JsonIgnore
    private Long diffLockedNumbers;
    private String summary;
    @JsonSerialize(using=DateSerializer.class)
    private Date createdDate;
    @JsonSerialize(using=DateSerializer.class)
    private Date lastUpdate;
    @JsonManagedReference
    private Set<Task> tasks;

    public Job(Long id, String name, String description, Date scheduledDate, Boolean now, String state, String owner, Integer totalTasks, Integer passedTasks, Integer failedTasks, String totalCoverage, String summary, Date createdDate, Date lastUpdate, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.now = now;
        this.state = state;
        this.owner = owner;
        this.totalTasks = totalTasks;
        this.passedTasks = passedTasks;
        this.failedTasks = failedTasks;
        this.totalCoverage = totalCoverage;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.tasks = tasks;
    }

    public Job(Long id, String name, String description, Date scheduledDate, Boolean now, String state, String owner, Integer totalTasks, Integer passedTasks, Integer failedTasks, String totalCoverage, String summary, Date createdDate, Date lastUpdate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.now = now;
        this.state = state;
        this.owner = owner;
        this.totalTasks = totalTasks;
        this.passedTasks = passedTasks;
        this.failedTasks = failedTasks;
        this.totalCoverage = totalCoverage;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
    }

    public Job(Long id, String name, String description, Date scheduledDate, Boolean now, String state, String owner, Integer totalTasks, Integer passedTasks, Integer failedTasks, String totalCoverage, Long lnNumbersInBccs, Long reservedLuckyNumbers, Long rolledBackNumbers, Long unlockedNumbers, Long lcNumbersInBccs, Long diffReservedNumbers, Long diffLockedNumbers, String summary, Date createdDate, Date lastUpdate, Set<Task> tasks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.scheduledDate = scheduledDate;
        this.now = now;
        this.state = state;
        this.owner = owner;
        this.totalTasks = totalTasks;
        this.passedTasks = passedTasks;
        this.failedTasks = failedTasks;
        this.totalCoverage = totalCoverage;
        this.lnNumbersInBccs = lnNumbersInBccs;
        this.reservedLuckyNumbers = reservedLuckyNumbers;
        this.rolledBackNumbers = rolledBackNumbers;
        this.unlockedNumbers = unlockedNumbers;
        this.lcNumbersInBccs = lcNumbersInBccs;
        this.diffReservedNumbers = diffReservedNumbers;
        this.diffLockedNumbers = diffLockedNumbers;
        this.summary = summary;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.tasks = tasks;
    }

    public Job() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Integer getPassedTasks() {
        return passedTasks;
    }

    public void setPassedTasks(Integer passedTasks) {
        this.passedTasks = passedTasks;
    }

    public Integer getFailedTasks() {
        return failedTasks;
    }

    public void setFailedTasks(Integer failedTasks) {
        this.failedTasks = failedTasks;
    }

    public String getTotalCoverage() {
        return totalCoverage;
    }

    public void setTotalCoverage(String totalCoverage) {
        this.totalCoverage = totalCoverage;
    }

    public Long getLnNumbersInBccs() {
        return lnNumbersInBccs;
    }

    public void setLnNumbersInBccs(Long lnNumbersInBccs) {
        this.lnNumbersInBccs = lnNumbersInBccs;
    }

    public Long getReservedLuckyNumbers() {
        return reservedLuckyNumbers;
    }

    public void setReservedLuckyNumbers(Long reservedLuckyNumbers) {
        this.reservedLuckyNumbers = reservedLuckyNumbers;
    }

    public Long getRolledBackNumbers() {
        return rolledBackNumbers;
    }

    public void setRolledBackNumbers(Long rolledBackNumbers) {
        this.rolledBackNumbers = rolledBackNumbers;
    }

    public Long getUnlockedNumbers() {
        return unlockedNumbers;
    }

    public void setUnlockedNumbers(Long unlockedNumbers) {
        this.unlockedNumbers = unlockedNumbers;
    }

    public Long getLcNumbersInBccs() {
        return lcNumbersInBccs;
    }

    public void setLcNumbersInBccs(Long lcNumbersInBccs) {
        this.lcNumbersInBccs = lcNumbersInBccs;
    }

    public Long getDiffReservedNumbers() {
        return diffReservedNumbers;
    }

    public void setDiffReservedNumbers(Long diffReservedNumbers) {
        this.diffReservedNumbers = diffReservedNumbers;
    }

    public Long getDiffLockedNumbers() {
        return diffLockedNumbers;
    }

    public void setDiffLockedNumbers(Long diffLockedNumbers) {
        this.diffLockedNumbers = diffLockedNumbers;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

}
