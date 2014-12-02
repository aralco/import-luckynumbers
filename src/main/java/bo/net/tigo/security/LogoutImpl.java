package bo.net.tigo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by aralco on 11/12/14.
 */
public class LogoutImpl implements LogoutSuccessHandler {
    private InMemoryTokenStore tokenstore;
    private static final Logger logger = LoggerFactory.getLogger(LogoutImpl.class);

    public void setTokenstore(InMemoryTokenStore tokenstore) {
        this.tokenstore = tokenstore;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest paramHttpServletRequest,
                                HttpServletResponse paramHttpServletResponse,
                                Authentication paramAuthentication) throws IOException,
            ServletException {
        removeAccess(paramHttpServletRequest);
        paramHttpServletResponse.getOutputStream().write("\n\tYou Have Logged Out successfully.".getBytes());

    }

    private void removeAccess(HttpServletRequest req) {
        String tokens = req.getHeader("Authorization");
        String value = tokens.substring(tokens.indexOf(" ")).trim();
        tokenstore.removeAccessToken(value);
        logger.info("Access Token Removed Successfully!!!!!!!!");
    }
}