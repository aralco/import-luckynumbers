package bo.net.tigo.service;

import bo.net.tigo.dao.UserDao;
import bo.net.tigo.model.User;
import bo.net.tigo.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by aralco on 11/12/14.
 */
@Service
public class SysUtilsService {
    @Autowired
    private UserDao userDao;

    private static final Logger logger = LoggerFactory.getLogger(SysUtilsService.class);

    @Transactional
    public User retrieveLoggedUser()    {
        return userDao.findByUsername(SecurityUtils.getCurrentUsername());
    }

}
