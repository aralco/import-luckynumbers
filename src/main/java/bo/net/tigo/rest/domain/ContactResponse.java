package bo.net.tigo.rest.domain;

import bo.net.tigo.model.Contact;

import java.util.List;

/**
 * Created by aralco on 11/14/14.
 */
public class ContactResponse {
    private List<Contact> contacts;

    public ContactResponse(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public ContactResponse() {
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
