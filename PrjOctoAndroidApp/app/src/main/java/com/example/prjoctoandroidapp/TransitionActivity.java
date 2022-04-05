package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.VideoView;

public class TransitionActivity extends AppCompatActivity {
    VideoView seaVideo;
    String nextActivityType;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        nextActivityType = getIntent().getStringExtra("activityType");

        seaVideo = findViewById(R.id.videoSea);
        seaVideo.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.sea);
        seaVideo.start();
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
                playSound();
            }

            public void onFinish() {
                seaVideo.stopPlayback();
                mp.stop();
                goNext();
                finish();

            }
        }.start();
    }
    private void playSound(){
        mp = MediaPlayer.create(this,R.raw.mixkitcartoonsurprisesuspense676);
        mp.start();
    }
    private void goNext(){

        switch (nextActivityType){
            case "imageExplorer":
                mp = MediaPlayer.create(this,R.raw.woo);
                mp.start();
                goToImageExplorer();
                break;
            case "questionsAnswers":
                mp = MediaPlayer.create(this,R.raw.are_you_ready);
                mp.start();
                goToQuestionsAndAnswers();
                break;
        }
    }
    private void goToImageExplorer() {
        Intent intent = new Intent(this, ImageExploreActivity.class);
        startActivity(intent);

    }

    private void goToQuestionsAndAnswers() {
        Intent intent = new Intent(this, QuestionsAndAnswersActivity.class);
        startActivity(intent);

    }
}