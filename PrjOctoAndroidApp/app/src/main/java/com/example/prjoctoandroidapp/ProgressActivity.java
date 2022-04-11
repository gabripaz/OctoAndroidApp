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
    Button btnExit;
    MediaPlayer mp;
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

        seaVideo.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.celebrateocto);
        seaVideo.start();
        mp = MediaPlayer.create(this,R.raw.kid_great_job_short);
        mp.start();
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent  = new Intent(view.getContext(), TransitionActivity.class);
               intent. putExtra("activityType", "afterQuestions");
               startActivity(intent);
                seaVideo.stopPlayback();
                finish();
            }
        });

        octoDB  = FirebaseDatabase.getInstance().getReference("users");
        Intent intent = getIntent();
        int point = intent.getIntExtra("TotalPoints",0);

        //mAuth = FirebaseAuth.getInstance();
        //DatabaseReference profileReference = octoDB.child(mAuth.getUid()).child("profiles").child("0"); //Later we need to get the profile name.
        String tempProfileID = "A4CtVkqMegTfIg1uow5NP6g40P92";// Delete!!! Did this because we dont login for test
        DatabaseReference profileReference = octoDB.child(tempProfileID).child("profiles").child("0"); //Delete!!!

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(Profile.class);
                tvKidsName.setText(profile.getNickName());
                tvResult.setText("Very good! you did "+tvResult.getText()+String.valueOf(point)+" points! \n And know you have total of" +String.valueOf(profile.getPoints()+point));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


    }
}