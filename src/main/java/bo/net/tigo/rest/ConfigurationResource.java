package bo.net.tigo.rest;

import bo.net.tigo.model.*;
import bo.net.tigo.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
@Controller
@RequestMapping(value = "/config",
        produces = "application/json")
public class ConfigurationResource {
    @Autowired
    private ConfigurationService configurationService;

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationResource.class);

    @RequestMapping(value = "/city", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<City> createCity(@RequestBody City city)   {
        City createdCity = configurationService.createCity(city);
        return new ResponseEntity<City>(createdCity, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/city/{cityId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<City> viewCity(@PathVariable Long cityId)   {
        logger.info("City for lookup:"+cityId);
        City city = configurationService.getCity(cityId);
        logger.info("viewCity: "+city);
        return new ResponseEntity<City>(city, HttpStatus.OK);
    }


    @RequestMapping(value = "/city/{cityId}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<City> updateCity(@PathVariable Long cityId, @RequestBody City city)   {
        City updatedCity = configurationService.updateCity(city);
        return new ResponseEntity<City>(updatedCity, HttpStatus.OK);
    }

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<City>> viewCities()   {
        List<City> cities = configurationService.getCities();
        return new ResponseEntity<List<City>>(cities, HttpStatus.OK);
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact)   {
        Contact createdContact = configurationService.createContact(contact);
        return new ResponseEntity<Contact>(createdContact, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/contact/{contactId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Contact> viewContact(@PathVariable Long contactId)   {
        logger.info("Contact for lookup:"+contactId);
        Contact contact = configurationService.getContact(contactId);
        logger.info("viewContact: "+contact);
        return new ResponseEntity<Contact>(contact, HttpStatus.OK);
    }


    @RequestMapping(value = "/contact/{contactId}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @RequestBody Contact contact)   {
        Contact updatedContact = configurationService.updateContact(contact);
        return new ResponseEntity<Contact>(updatedContact, HttpStatus.OK);
    }

    @RequestMapping(value = "/contact", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Contact>> viewContacts()   {
        List<Contact> contacts = configurationService.getContacts();
        return new ResponseEntity<List<Contact>>(contacts, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<User> createUser(@RequestBody User user)   {
        User createdUser = configurationService.createUser(user);
        return new ResponseEntity<User>(createdUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> viewUser(@PathVariable Long userId)   {
        User user = configurationService.getUser(userId);
        logger.info("viewUser:"+user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user)   {
        User updatedUser = configurationService.updateUser(user);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<User>> viewUsers()   {
        List<User> users = configurationService.getUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/accesslog", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<AccessLog>> viewAccessLogs()   {
        List<AccessLog> accessLogs = configurationService.getAccessLogs();
        return new ResponseEntity<List<AccessLog>>(accessLogs, HttpStatus.OK);
    }

}
