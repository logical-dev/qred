package se.qred.loan.db;

import com.google.common.base.Objects;
import se.qred.loan.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private List<User> users;

    public UserRepository() {
        this.users = new ArrayList<>();
    }

    public User findByUsername(String username) {
        for (User user : users) {
            if (Objects.equal(user.getUsername(), username)){
                return user;
            }
        }
        return null;
    }

    public void addDummyData() {
        users.add(new User("1", "fay", "1234"));
    }
}
