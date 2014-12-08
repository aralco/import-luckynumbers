package bo.net.tigo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aralco on 10/29/14.
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    private static final String PASSWORD = "password";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String CLIENT_CREDENTIALS = "client_credentials";
    private String clientId;
    private String clientSecret;
    private static final Logger logger = LoggerFactory.getLogger(ClientDetailsServiceImpl.class);

    @Override
    public ClientDetails loadClientByClientId(String clientId)
            throws OAuth2Exception {
        logger.info("ClientDetailsServiceImpl:loadClientByClientId:"+clientId);

        if (clientId.equals(this.clientId))
        {
            List<String> authorizedGrantTypes = new ArrayList<String>();
            authorizedGrantTypes.add(PASSWORD);
            authorizedGrantTypes.add(REFRESH_TOKEN);
            authorizedGrantTypes.add(CLIENT_CREDENTIALS);

            BaseClientDetails clientDetails = new BaseClientDetails();
            clientDetails.setClientId(this.clientId);
            clientDetails.setClientSecret(clientSecret);
            clientDetails.setAuthorizedGrantTypes(authorizedGrantTypes);

            return clientDetails;
        }
        else {
            throw new NoSuchClientException("No client recognized with clientId: "
                    + clientId);
        }
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

}