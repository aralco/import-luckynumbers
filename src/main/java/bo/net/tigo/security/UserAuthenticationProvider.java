package bo.net.tigo.security;

import bo.net.tigo.dao.UserDao;
import bo.net.tigo.model.Action;
import bo.net.tigo.model.User;
import bo.net.tigo.service.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aralco on 10/29/14.
 */
@Service
@Transactional
public class UserAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthenticationProvider.class);

    @Autowired
    ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuditService auditService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        logger.info("Authentication to authenticate:" + authentication);
        if(authentication!=null)    {
            UserDetails userDetails = loadUserByUsername(authentication.getPrincipal().toString());
            if(userDetails==null)
                throw new UsernameNotFoundException("El usuario "+authentication.getPrincipal().toString()+" no existe.");
//PROD MODE
            authentication = activeDirectoryLdapAuthenticationProvider.authenticate(authentication);
            if(authentication!=null && authentication.isAuthenticated())    {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
                logger.info("User successfully authenticated - authenticate:"+usernamePasswordAuthenticationToken);
                auditService.audit(Action.AUTENTICACION);
                return usernamePasswordAuthenticationToken;
//DEV MODE
//            if(authentication.getPrincipal().equals("sysportal")&& authentication.getCredentials().equals("Sysp0rt4l")) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
//                logger.info("User successfully authenticated - authenticate:" + usernamePasswordAuthenticationToken);
//                auditService.audit(Action.AUTENTICACION);
//                return usernamePasswordAuthenticationToken;
//            }
//            else if(authentication.getPrincipal().equals("user1")&& authentication.getCredentials().equals("user1")) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
//                logger.info("User successfully authenticated - authenticate:"+usernamePasswordAuthenticationToken);
//                auditService.audit(Action.AUTENTICACION);
//                return usernamePasswordAuthenticationToken;
//            } else if(authentication.getPrincipal().equals("user2")&& authentication.getCredentials().equals("user2")) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
//                logger.info("User successfully authenticated - authenticate:"+usernamePasswordAuthenticationToken);
//                auditService.audit(Action.AUTENTICACION);
//                return usernamePasswordAuthenticationToken;
            } else {
                throw new BadCredentialsException("Credenciales de acceso no válidas.");
            }
        } else  {
            throw new AuthenticationCredentialsNotFoundException("Authentication object is null in SecurityContext");
        }
    }

    public boolean supports(Class<?> arg0) {
        return true;
    }

    private UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("loadUserByUsername:"+username);
        User domainUser = userDao.findByUsername(username);
        if(domainUser==null)    {
            //if user doesn't exist in database must go away
            return null;
        }
        if(!domainUser.getEnabled())    {
            throw new DisabledException("El usuario "+domainUser.getUsername()+", no está habilitado.");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(0);
        authorities.add(new LuckyNumbersGrantedAuthorities(domainUser.getRole()));
        return new org.springframework.security.core.userdetails.User(
                domainUser.getUsername(),
                "",
                domainUser.getEnabled(),
                domainUser.getEnabled(),
                domainUser.getEnabled(),
                domainUser.getEnabled(),
                authorities
        );
    }


}