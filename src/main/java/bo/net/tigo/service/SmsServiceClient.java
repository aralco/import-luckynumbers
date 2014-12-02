package bo.net.tigo.service;

import bo.net.tigo.wsdl.EnviarSms;
import bo.net.tigo.wsdl.EnviarSmsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

/**
 * Created by aralco on 11/27/14.
 */
public class SmsServiceClient extends WebServiceGatewaySupport {
    private static final Logger logger = LoggerFactory.getLogger(SmsServiceClient.class);

    public void sendSmsNotification(String phone, int senderShort, String message)    {
        EnviarSms request = new EnviarSms();
        request.setNumeroTelefono(phone);
        request.setCorto(senderShort);
        request.setMensaje(message);
        logger.info("SMS request:" + request.toString());
        EnviarSmsResponse response = (EnviarSmsResponse)getWebServiceTemplate().marshalSendAndReceive(
                request,
                new SoapActionCallback("http://wscrmdev.tigo.net.bo:8080/CommitEnvioSmsWSv2/EnvioSMS")
        );
        if(response==null)  {
            logger.warn("No response from sms web service.");
        } else {
            logger.info("SMS response:"+response.getReturn().getGeneralResponse().toString());
        }
    }
}
