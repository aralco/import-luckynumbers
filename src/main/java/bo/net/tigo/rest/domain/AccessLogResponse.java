package bo.net.tigo.rest.domain;

import bo.net.tigo.model.AccessLog;

import java.util.List;

/**
 * Created by aralco on 11/14/14.
 */
public class AccessLogResponse {

    private List<AccessLog> accessLogs;

    public AccessLogResponse(List<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }

    public AccessLogResponse() {
    }

    public List<AccessLog> getAccessLogs() {
        return accessLogs;
    }

    public void setAccessLogs(List<AccessLog> accessLogs) {
        this.accessLogs = accessLogs;
    }
}
