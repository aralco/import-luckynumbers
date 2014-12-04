package bo.net.tigo.service;

import bo.net.tigo.wsdl.EnviarSms;
import bo.net.tigo.wsdl.EnviarSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;

/**
 * Created by aralco on 11/27/14.
 */
public class SmsServiceClient extends WebServiceGatewaySupport {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceClient.class);
    private int senderShort;

    public void sendSmsNotification(String phone, String message)    {
        try {
            EnviarSms request = new EnviarSms();
            request.setNumeroTelefono(phone);
            request.setCorto(senderShort);
            request.setMensaje(message);
            logger.info("SMS request:" + request.toString());
            JAXBElement response = (JAXBElement)getWebServiceTemplate().marshalSendAndReceive(request);
            if(response==null)  {
                logger.warn("No response received from sms web service.");
            } else {
                if(response.getValue() instanceof EnviarSmsResponse) {
                    EnviarSmsResponse enviarSmsResponse = (EnviarSmsResponse)response.getValue();
                    if(enviarSmsResponse.getReturn().getGeneralResponse().getStatus().equals("OK")){
                        logger.info("SMS has been succesfully sent to "+phone+", with message from sms service: "
                                +enviarSmsResponse.getReturn().getGeneralResponse().getMessage());
                    } else  {
                        logger.warn("SMS could not been sent to "+phone+", with message from sms service: "
                                +enviarSmsResponse.getReturn().getGeneralResponse().getMessage());
                    }
                }
            }

        }catch(Exception e)    {
            logger.warn("Could not send SMS notification.");
        }
    }

    public void setSenderShort(int senderShort) {
        this.senderShort = senderShort;
    }
}
