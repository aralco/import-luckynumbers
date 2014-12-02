package bo.net.tigo.dao;

import bo.net.tigo.model.AccessLog;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface AccessLogDao {
    public void save(AccessLog accessLog);
    public List<AccessLog> findAll();
}
