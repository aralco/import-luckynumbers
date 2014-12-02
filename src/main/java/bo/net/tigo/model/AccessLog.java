package bo.net.tigo.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

import java.util.Date;

/**
 * Created by aralco on 11/8/14.
 */
public class AccessLog {
    private Long id;
    @JsonSerialize(using=DateSerializer.class)
    private Date timestamp;
    private String user;
    private String action;
    private String description;

    public AccessLog() {
    }

    public AccessLog(Long id, Date timestamp, String user, String action, String description) {
        this.id = id;
        this.timestamp = timestamp;
        this.user = user;
        this.action = action;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
