package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {

    private String profileID;
    private String nickName;
    private String avatarUrl;
    private int age;
    private int points;

    private ArrayList<Reward> rewards;
    private ArrayList<RunOfQuestions> listOfRuns;

    public Profile() {
    }

    public Profile(String nickName, String avatarUrl, int age, ArrayList<Reward> rewards) {
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.age = age;
        this.points = 0;
        this.rewards = rewards;
        this.listOfRuns = new ArrayList<RunOfQuestions>();
    }



    public String getNickName() {return nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getAvatarUrl() { return avatarUrl; }

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl;}

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age;}

    public int getPoints() { return points; }

    public void setPoints(int points) {this.points = points;  }

    public ArrayList<Reward> getRewards() { return rewards;}

    public void setRewards(ArrayList<Reward> rewards) {this.rewards = rewards;}

    public ArrayList<RunOfQuestions> getListOfRuns() {return listOfRuns;}

    public void setListOfRuns(ArrayList<RunOfQuestions> listOfRuns) {this.listOfRuns = listOfRuns;}

    public String getProfileID() {return profileID;}

    public void setProfileID(String profileID) {this.profileID = profileID;}
}
