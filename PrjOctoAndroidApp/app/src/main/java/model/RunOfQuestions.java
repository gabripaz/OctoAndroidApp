package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RunOfQuestions implements Serializable {
    private ArrayList<Question> listOfQuestions;
    private LocalDateTime date;
    private EnumStatus status;
    private Integer totalPoints;
    private int id;

    public RunOfQuestions(ArrayList<Question> listOfQuestions, LocalDateTime date, EnumStatus status, Integer totalPoints, int id) {
        this.listOfQuestions = listOfQuestions;
        this.date = date;
        this.status = status;
        this.totalPoints = totalPoints;
        this.id = id;
    }

    public RunOfQuestions(int id) {
        this.id = id;
        this.date = getDate();
        this.totalPoints = 0;
        status = EnumStatus.INPROGRESS;
        listOfQuestions = new ArrayList<Question>();
    }

    public ArrayList<Question> getListOfQuestions() {return listOfQuestions;}

    public void setListOfQuestions(ArrayList<Question> listOfQuestions) {this.listOfQuestions = listOfQuestions;}

    public LocalDateTime getDate() {return date;}

    public void setDate(LocalDateTime date) {this.date = date; }

    public EnumStatus getStatus() {return status;}

    public void setStatus(EnumStatus status) {this.status = status;}

    public Integer getTotalPoints() {return totalPoints;}

    public void setTotalPoints(Integer totalPoints) {this.totalPoints = totalPoints;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}
}
