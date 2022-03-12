package model;

import com.google.type.DateTime;

import java.util.ArrayList;

public class RunOfQuestions {
    private ArrayList<Question> listOfQuestions;
    private DateTime date;
    private EnumStatus status;
    private Integer totalPoints;
    private int id;

    public RunOfQuestions(ArrayList<Question> listOfQuestions, DateTime date, EnumStatus status, Integer totalPoints, int id) {
        this.listOfQuestions = listOfQuestions;
        this.date = date;
        this.status = status;
        this.totalPoints = totalPoints;
        this.id = id;
    }

    public ArrayList<Question> getListOfQuestions() {return listOfQuestions;}

    public void setListOfQuestions(ArrayList<Question> listOfQuestions) {this.listOfQuestions = listOfQuestions;}

    public DateTime getDate() {return date;}

    public void setDate(DateTime date) {this.date = date; }

    public EnumStatus getStatus() {return status;}

    public void setStatus(EnumStatus status) {this.status = status;}

    public Integer getTotalPoints() {return totalPoints;}

    public void setTotalPoints(Integer totalPoints) {this.totalPoints = totalPoints;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
}
