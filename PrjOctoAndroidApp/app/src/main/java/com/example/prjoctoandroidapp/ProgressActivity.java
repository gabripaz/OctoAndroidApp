package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.Profile;

public class ProgressActivity extends AppCompatActivity{

    private TextView tvKidsName,tvResult;
    VideoView seaVideo;
    Button btnExit, btnPlayAgain;
    MediaPlayer mp,main;

    //Objects
    private FirebaseAuth mAuth; //get the current user
    private DatabaseReference octoDB; //reference to our Database
    private Profile profile; //current profile
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        Initialization();
    }

    private void Initialization() {
        tvKidsName =  findViewById(R.id.tvKidsName);
        tvResult = findViewById(R.id.tvResult);
        seaVideo = findViewById(R.id.videoResult);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        seaVideo.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.celebrateocto);
        seaVideo.start();
        mp = MediaPlayer.create(this,R.raw.kid_great_job_short);
        mp.start();
        main = MediaPlayer.create(this,R.raw.cheerful_music);
        main.setLooping(true);
        main.start();
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent  = new Intent(view.getContext(), TransitionActivity.class);
               intent. putExtra("activityType", "afterQuestions");
               intent. putExtra("profileID", profile.getProfileID());
               startActivity(intent);
                seaVideo.stopPlayback();
                main.stop();
                finish();
            }
        });

        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(view.getContext(), TransitionActivity.class);
                intent. putExtra("activityType", "questionsAnswers");
                intent. putExtra("profileID", profile.getProfileID());
                startActivity(intent);
                seaVideo.stopPlayback();
                main.stop();
                finish();
            }
        });

        octoDB  = FirebaseDatabase.getInstance().getReference("users");
        Intent intent = getIntent();
        int point = intent.getIntExtra("TotalPoints",0);
        String profileID = intent.getStringExtra("profileID");

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference profileReference = octoDB.child(mAuth.getUid()).child("profiles").child(profileID); //Later we need to get the profile name.

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(Profile.class);
                profile.setProfileID(getIntent().getStringExtra("profileID"));
                tvKidsName.setText(profile.getNickName());
                tvResult.setText(
                        "Very good! you did "+ String.valueOf(point)+" points! " +
                        "\n And now you have total of " + String.valueOf(profile.getPoints()) + " points!");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }
}