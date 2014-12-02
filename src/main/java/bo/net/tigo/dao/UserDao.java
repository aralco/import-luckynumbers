package bo.net.tigo.dao;

import bo.net.tigo.model.User;

import java.util.List;

/**
 * Created by aralco on 11/9/14.
 */
public interface UserDao {
    public void save(User user);
    public void update(User user);
    public User findOne(Long userId);
    public User findByUsername(String username);
    public List<User> findAll();
}
