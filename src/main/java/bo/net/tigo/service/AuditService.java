package bo.net.tigo.service;

import bo.net.tigo.dao.AccessLogDao;
import bo.net.tigo.model.AccessLog;
import bo.net.tigo.model.Action;
import bo.net.tigo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by aralco on 12/7/14.
 */
@Service
public class AuditService {

    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    @Autowired
    private AccessLogDao accessLogDao;

    @Transactional
    public void audit(Action action)    {
        AccessLog accessLog = new AccessLog();
        accessLog.setTimestamp(new Date());
        accessLog.setUser(SecurityUtils.getCurrentUsername());
        accessLog.setAction(action.name());
        accessLog.setDescription(action.name());
        accessLogDao.save(accessLog);
        logger.info("Audit operation: "+accessLog);
    }
}
