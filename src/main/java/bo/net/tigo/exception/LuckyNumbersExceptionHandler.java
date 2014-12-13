package bo.net.tigo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
    private static final Logger logger = LoggerFactory.getLogger(LuckyNumbersExceptionHandler.class);

    @ExceptionHandler(LuckyNumbersGenericException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(LuckyNumbersGenericException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getErrorCode(), e.getErrorMessage());
        logger.warn("ErrorCode:"+e.getErrorCode()+", ErrorMessage:"+e.getErrorMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.valueOf(Integer.valueOf(e.getErrorCode())));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(UsernameNotFoundException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(DisabledException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(BadCredentialsException e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleException(Exception e)   {
        ErrorMessage errorMessage = new ErrorMessage(e.getClass().getSimpleName(), e.getMessage());
        return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
