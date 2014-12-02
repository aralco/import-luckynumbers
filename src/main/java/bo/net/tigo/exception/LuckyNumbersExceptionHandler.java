package bo.net.tigo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Created by aralco on 11/10/14.
 */
@ControllerAdvice
public class LuckyNumbersExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(LuckyNumbersGenericException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(LuckyNumbersGenericException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getErrorMessage());
        ResponseEntity<ErrorMessage> responseEntity = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.valueOf(e.getErrorCode()));
        return responseEntity;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(UsernameNotFoundException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        ResponseEntity<ErrorMessage> responseEntity = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(Exception e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
        ResponseEntity<ErrorMessage> responseEntity = new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

}
