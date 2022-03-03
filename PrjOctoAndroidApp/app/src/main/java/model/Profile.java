package model;

public class Profile {
    private String nickName;
    private String avatarUrl;
    private int age;
    private int points;
    private Rewards rewards;

    public Profile() {
    }

    public Profile(String nickName, String avatarUrl, int age, Rewards rewards) {
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.age = age;
        this.points = 0;
        this.rewards = rewards;
    }

    public Profile(String nickName, String avatarUrl, int age, int points, Rewards rewards) {
        this.nickName = nickName;
        this.avatarUrl = avatarUrl;
        this.age = age;
        this.points = points;
        this.rewards = rewards;
    }

    public String getNickName() {return nickName; }

    public void setNickName(String nickName) { this.nickName = nickName; }

    public String getAvatarUrl() { return avatarUrl; }

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl;}

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age;}

    public int getPoints() { return points; }

    public void setPoints(int points) {this.points = points;  }

    public Rewards getRewards() { return rewards;}

    public void setRewards(Rewards rewards) {this.rewards = rewards;}
}
