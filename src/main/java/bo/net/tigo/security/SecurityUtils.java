package bo.net.tigo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by aralco on 11/11/14.
 */
public class SecurityUtils {
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);


    public static Authentication getCurrentAuthenticateObject()  {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static UserDetails getCurrentUserDetails()  {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return (UserDetails)principal;
        }
        return null;
    }

    public static String getCurrentUsername()  {
        //DEV MODE
        logger.info("Authenticate:SecurityContextHolder.getContext().getAuthentication()="+SecurityContextHolder.getContext().getAuthentication());
        logger.info("AuthenticateSecurityContextHolder.getContext().getAuthentication().getPrincipal()="+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        logger.info("AuthenticateSecurityContextHolder.getContext().getAuthentication().getPrincipal()="+SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        logger.info("Authenticate=SecurityContextHolder.getContext().getAuthentication().getCredentials()="+SecurityContextHolder.getContext().getAuthentication().getCredentials());
        logger.info("Authenticate=SecurityContextHolder.getContext().getAuthentication().getAuthorities()="+SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        logger.info("Authenticate=SecurityContextHolder.getContext().getAuthentication().getDetails()="+SecurityContextHolder.getContext().getAuthentication().getDetails());
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        //PROD MODE
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = null;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails)principal).getUsername();
//        } else
//            throw new UsernameNotFoundException("user doesn't exists.");
//        return username;

    }
}
