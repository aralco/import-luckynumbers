package bo.net.tigo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by aralco on 11/8/14.
 */
public class City {
    private Long id;
        private Integer code;
        private String name;
        private String description;
        private Boolean enabled;
        @JsonIgnore
        private Date createdDate;
        @JsonIgnore
        private Date lastUpdate;

    public City(Long id, Integer code, String name, String description, Boolean enabled, Date createdDate, Date lastUpdate) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
    }

    public City() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
