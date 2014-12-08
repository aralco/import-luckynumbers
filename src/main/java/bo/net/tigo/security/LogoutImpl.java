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
import java.nio.charset.Charset;

/**
 * Created by aralco on 11/12/14.
 */
public class LogoutImpl implements LogoutSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogoutImpl.class);
    private static final String UTF_8 = "UTF-8";
    private InMemoryTokenStore tokenStore;

    public void setTokenStore(InMemoryTokenStore tokenstore) {
        this.tokenStore = tokenstore;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest paramHttpServletRequest,
                                HttpServletResponse paramHttpServletResponse,
                                Authentication paramAuthentication) throws IOException,
            ServletException {
        removeAccess(paramHttpServletRequest);
        paramHttpServletResponse.getOutputStream().write("\n\tYou Have Logged Out successfully.".getBytes(Charset.forName(UTF_8)));

    }

    private void removeAccess(HttpServletRequest req) {
        String tokens = req.getHeader("Authorization");
        String value = tokens.substring(tokens.indexOf(" ")).trim();
        tokenStore.removeAccessToken(value);
        logger.info("Access Token Removed Successfully!!");
    }
}