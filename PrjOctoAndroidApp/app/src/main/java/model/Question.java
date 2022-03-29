package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private String answer, statement;
    private String musics,sounds;
    private long id;
    private long minage;
    private long points;
    private ArrayList<String> options,images;

    public Question(String answer, String statement, ArrayList<String> images, String musics, String sounds, long id, long minage, long points, ArrayList<String> options) {
        this.answer = answer;
        this.statement = statement;
        this.images = images;
        this.musics = musics;
        this.sounds = sounds;
        this.id = id;
        this.minage = minage;
        this.points = points;
        this.options = options;
    }
    public Question(){}

    public String getAnswer() {return answer;}

    public void setAnswer(String answer) {this.answer = answer;}

    public String getStatement() {return statement;}

    public void setStatement(String statement) {this.statement = statement;}

    public ArrayList<String> getImages() {return images;}

    public void setImages(ArrayList<String> images) {this.images = images;}

    public String getMusics() {return musics;}

    public void setMusics(String musics) {this.musics = musics;}

    public String getSounds() {return sounds;}

    public void setSounds(String sounds) {this.sounds = sounds;}

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public long getMinage() {return minage;}

    public void setMinage(long minage) {this.minage = minage;}

    public long getPoints() {return points;}

    public void setPoints(long points) {this.points = points;}

    public ArrayList<String> getOptions() {return options;}

    public void setOptions(ArrayList<String> options) {this.options = options;}
}
