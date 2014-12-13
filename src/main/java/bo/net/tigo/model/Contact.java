package bo.net.tigo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by aralco on 11/8/14.
 */
public class Contact {
    private Long id;
    private String name;
    private String email1;
    private String email2;
    private String phone1;
    private String phone2;
    private String phone3;
    private String description;
    @JsonIgnore
    private Date createdDate;
    @JsonIgnore
    private Date lastUpdate;
    private Boolean enabled;

    public Contact(Long id, String name, String email1, String email2, String phone1, String phone2, String phone3, String description, Date createdDate, Date lastUpdate, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.email1 = email1;
        this.email2 = email2;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
        this.description = description;
        this.createdDate = createdDate;
        this.lastUpdate = lastUpdate;
        this.enabled = enabled;
    }

    public Contact() {
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

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
