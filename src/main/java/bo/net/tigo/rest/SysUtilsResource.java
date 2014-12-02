package bo.net.tigo.rest;

import bo.net.tigo.model.User;
import bo.net.tigo.service.SysUtilsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by aralco on 11/12/14.
 */
@Controller
@RequestMapping(value = "/sysutils",
        produces = "application/json")
public class SysUtilsResource {

    @Autowired
    private SysUtilsService sysUtilsService;

    @RequestMapping(value = "/loggeduser", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> retrieveLoggedUser()   {
        User loggedUser = sysUtilsService.retrieveLoggedUser();
        return new ResponseEntity<User>(loggedUser, HttpStatus.OK);

    }

}
