package model;

import java.io.Serializable;

public class OctoUser implements Serializable {
    private String userId;
    private String username;
    private  String fullname;
    private  String email;
    private String password;

    public OctoUser() {
    }

    public OctoUser(String username, String fullname, String email) {
        this.username = username;
        this.fullname = fullname;
        this.email = email;

    }

    public OctoUser(String userId, String username, String fullname, String email, String password) {
        this.userId = userId;
        this.username = username;
        this.fullname = fullname;
        this.email    = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
