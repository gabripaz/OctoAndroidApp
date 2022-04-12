package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RunOfQuestions implements Serializable {
    private ArrayList<Question> listOfQuestions;
    private String date;
    private EnumStatus status;
    private Integer totalPoints;
    private int id;

    public RunOfQuestions(ArrayList<Question> listOfQuestions, String date, EnumStatus status, Integer totalPoints, int id) {
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

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date; }

    public EnumStatus getStatus() {return status;}

    public void setStatus(EnumStatus status) {this.status = status;}

    public Integer getTotalPoints() {return totalPoints;}

    public void setTotalPoints(Integer totalPoints) {this.totalPoints = totalPoints;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getListofQuestionsIds(){
        ArrayList<String> questions = new ArrayList<String>();
        for ( Question question : listOfQuestions
             ) {
            questions.add(String.valueOf(question.getId()));
        }
        return String.join(",",questions);
    }

    public LocalDateTime TodaysDate(){
        return LocalDateTime.now();
    }
}
