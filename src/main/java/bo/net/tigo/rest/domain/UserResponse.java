package bo.net.tigo.rest.domain;

import bo.net.tigo.model.User;

import java.util.List;

/**
 * Created by aralco on 11/14/14.
 */
public class UserResponse {
    private List<User> users;

    public UserResponse(List<User> users) {
        this.users = users;
    }

    public UserResponse() {
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
