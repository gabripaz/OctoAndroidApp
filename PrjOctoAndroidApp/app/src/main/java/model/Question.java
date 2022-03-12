package model;

import java.io.Serializable;

public class Question implements Serializable {
    private String answer, statement;
    private String images,musics,sounds;
    private int id,minage,points;

    public Question(String answer, String statement, String images, String musics, String sounds, int id, int minage, int points) {
        this.answer = answer;
        this.statement = statement;
        this.images = images;
        this.musics = musics;
        this.sounds = sounds;
        this.id = id;
        this.minage = minage;
        this.points = points;
    }

    public String getAnswer() {return answer;}

    public void setAnswer(String answer) {this.answer = answer;}

    public String getStatement() {return statement;}

    public void setStatement(String statement) {this.statement = statement;}

    public String getImages() {return images;}

    public void setImages(String images) {this.images = images;}

    public String getMusics() {return musics;}

    public void setMusics(String musics) {this.musics = musics;}

    public String getSounds() {return sounds;}

    public void setSounds(String sounds) {this.sounds = sounds;}

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getMinage() {return minage;}

    public void setMinage(int minage) {this.minage = minage;}

    public int getPoints() {return points;}

    public void setPoints(int points) {this.points = points;}
}
