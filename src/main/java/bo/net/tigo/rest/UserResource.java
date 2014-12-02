package bo.net.tigo.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by aralco on 10/29/14.
 */

@Controller
@RequestMapping(value = "/userresource")
public class UserResource {
    @RequestMapping(value = "/userprofile", method = RequestMethod.GET)
    public String getUserProfile(){
        return "Welcome in protected Area. User enabled.";
    }
}
