package bo.net.tigo.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by aralco on 12/20/14.
 */
@Controller
@RequestMapping(value = "/error")
public class ErrorController {
    @RequestMapping(value = "/not-found", method = RequestMethod.GET)
    public ResponseEntity<String> notFound() {
        return new ResponseEntity<String>("<h1>Recurso no encontrado.</h1>", HttpStatus.NOT_FOUND);
    }
}
