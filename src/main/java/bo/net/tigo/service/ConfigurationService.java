package bo.net.tigo.service;

import bo.net.tigo.dao.AccessLogDao;
import bo.net.tigo.dao.CityDao;
import bo.net.tigo.dao.ContactDao;
import bo.net.tigo.dao.UserDao;
import bo.net.tigo.exception.LuckyNumbersGenericException;
import bo.net.tigo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by aralco on 10/22/14.
 */
@Service
public class ConfigurationService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private AccessLogDao accessLogDao;
    @Autowired
    private AuditService auditService;

    @Transactional
    public User createUser(User user)  {
        auditService.audit(Action.CREAR_USUARIO);
        user.setCreatedDate(new Date());
        userDao.save(user);
        return user;
    }

    @Transactional
    public User getUser(Long userId)  {
        return userDao.findOne(userId);
    }

    @Transactional
    public User updateUser(User user)  {
        auditService.audit(Action.EDITAR_USUARIO);
        User persistedUser = userDao.findOne(user.getId());
        if(persistedUser==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"El usuario relacionado no pudo ser encontrado.");
        }
        persistedUser.setName(user.getName());
        persistedUser.setUsername(user.getUsername());
        persistedUser.setRole(user.getRole());
        persistedUser.setDescription(user.getDescription());
        persistedUser.setEmail1(user.getEmail1());
        persistedUser.setPhone1(user.getPhone1());
        persistedUser.setEnabled(user.getEnabled());
        persistedUser.setLastUpdate(new Date());
        userDao.update(persistedUser);
        return persistedUser;
    }

    @Transactional
    public List<User> getUsers()  {
        return userDao.findAll();
    }

    @Transactional
    public Contact createContact(Contact contact)  {
        auditService.audit(Action.CREAR_CONTACTO);
        contact.setCreatedDate(new Date());
        contactDao.save(contact);
        return contact;
    }

    @Transactional
    public Contact getContact(Long contactId)  {
        return contactDao.findOne(contactId);
    }

    @Transactional
    public Contact updateContact(Contact contact)  {
        auditService.audit(Action.EDITAR_CONTACTO);
        Contact persistedContact = contactDao.findOne(contact.getId());
        if(persistedContact==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"El contacto relacionado no pudo ser encontrado.");
        }
        persistedContact.setName(contact.getName());
        persistedContact.setDescription(contact.getDescription());
        persistedContact.setEmail1(contact.getEmail1());
        persistedContact.setPhone1(contact.getPhone1());
        persistedContact.setEnabled(contact.getEnabled());
        persistedContact.setLastUpdate(new Date());
        contactDao.update(persistedContact);
        return persistedContact;
    }

    @Transactional
    public List<Contact> getContacts()  {
        return contactDao.findAll();
    }

    @Transactional
    public City createCity(City city)  {
        auditService.audit(Action.CREAR_CIUDAD);
        city.setCreatedDate(new Date());
        cityDao.save(city);
        return city;
    }

    @Transactional
    public City getCity(Long cityId)  {
        return cityDao.findOne(cityId);
    }


    @Transactional
    public City updateCity(City city)  {
        auditService.audit(Action.EDITAR_CIUDAD);
        City persistedCity = cityDao.findOne(city.getId());
        if(persistedCity==null)   {
            throw new LuckyNumbersGenericException(HttpStatus.NOT_FOUND.toString(),"La ciudad relacionado no pudo ser encontrada.");
        }
        persistedCity.setCode(city.getCode());
        persistedCity.setName(city.getName());
        persistedCity.setDescription(city.getDescription());
        persistedCity.setEnabled(city.getEnabled());
        persistedCity.setLastUpdate(new Date());
        cityDao.update(persistedCity);
        return persistedCity;
    }

    @Transactional
    public List<City> getCities()  {
        return cityDao.findAll();
    }

    @Transactional
    public List<AccessLog> getAccessLogs()  {
        return accessLogDao.findAll();
    }

}
