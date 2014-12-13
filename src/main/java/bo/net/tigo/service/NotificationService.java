package bo.net.tigo.service;

import bo.net.tigo.dao.ContactDao;
import bo.net.tigo.dao.UserDao;
import bo.net.tigo.model.Contact;
import bo.net.tigo.model.State;
import bo.net.tigo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by aralco on 12/4/14.
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
//    @Autowired
//    private SmsServiceClient smsServiceClient;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private UserDao userDao;
    @Value("${tigo.soap.webservice.sms.messageJobOK}")
    private String messageOK;
    @Value("${tigo.soap.webservice.sms.messageJobError}")
    private String messageError;

    public void sendNotification(String username, String jobName, State state)  {
        User user = userDao.findByUsername(username);
        List<Contact> contactList = contactDao.findEnabled(true);
        String message = getMessage(jobName, state);
        logger.info("Sending notification to job owner:"+username);
//        smsServiceClient.sendSmsNotification(user.getPhone1(), message);
        if(contactList!=null && contactList.size()>0)   {
            for(Contact contact : contactList)  {
                logger.info("Sending notification to contact:"+contact.getName());
//                smsServiceClient.sendSmsNotification(contact.getPhone1(), message);
            }
        }
    }

    private String getMessage(String jobName, State state) {
        String message;
        switch (state)  {
            case DONE:
                message = messageOK;
                break;
            case CRITERIA_ACCEPTANCE:
                message = messageError;
                break;
            default:
                message = "";
                break;
        }
        return message.replace("@",jobName);
    }

}
