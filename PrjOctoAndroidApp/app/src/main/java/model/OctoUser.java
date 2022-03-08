package model;

import java.io.Serializable;
import java.util.ArrayList;

public class OctoUser implements Serializable {
    private String userId;
    private String username;
    private  String fullName;
    private  String email;
    private String password;
    private ArrayList<Profile> profiles;
    public OctoUser() {
    }

    public OctoUser(String username, String fullName, String email,  ArrayList<Profile>  profile) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.profiles = profile;

    }

    public OctoUser(String userId, String username, String fullName, String email, String password, ArrayList<Profile> profiles) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.profiles = profiles;
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
        return fullName;
    }

    public void setFullname(String fullName) {
        this.fullName = fullName;
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

    public ArrayList<Profile> getProfiles() {return profiles;}

    public void setProfiles(ArrayList<Profile> profiles) {this.profiles = profiles;}

}
