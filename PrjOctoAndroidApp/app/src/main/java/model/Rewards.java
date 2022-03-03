package model;

public class Rewards {
    private String badgeName;
    private int points;

    public Rewards() {
    }
    public Rewards(String badgeName, int points) {
        this.badgeName = badgeName;
        this.points = points;
    }

    public String getBadgeName() { return badgeName; }

    public void setBadgeName(String badgeName) {this.badgeName = badgeName; }

    public int getPoints() { return points;}

    public void setPoints(int points) { this.points = points; }
}
