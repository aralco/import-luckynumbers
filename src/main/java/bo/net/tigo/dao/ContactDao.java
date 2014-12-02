package bo.net.tigo.dao;

import bo.net.tigo.model.Contact;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface ContactDao {
    public void save(Contact contact);
    public void update(Contact contact);
    public Contact findOne(Long contactId);
    public List<Contact> findAll();

}
