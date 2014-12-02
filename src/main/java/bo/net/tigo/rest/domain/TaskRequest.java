package bo.net.tigo.rest.domain;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by aralco on 10/29/14.
 */
public class TaskRequest {
    @NotEmpty
    private String type; //{FROZEN;FREE}
    @NotNull
    private Integer city;
    @NotEmpty
    private String from;
    @NotEmpty
    private String to;

    public TaskRequest(String type, Integer city, String from, String to) {
        this.type = type;
        this.city = city;
        this.from = from;
        this.to = to;
    }

    public TaskRequest() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "TaskRequest{" +
                "type='" + type + '\'' +
                ", city=" + city +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
