package se.qred.loan.model;

import java.security.Principal;

public class User implements Principal {
    private String id;
    private String username;
    private String password;

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return "used-in-authenticator";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
