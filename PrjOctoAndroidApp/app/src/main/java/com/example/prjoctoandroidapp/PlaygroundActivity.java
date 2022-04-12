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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

import model.Profile;
import model.Question;
import model.RunOfQuestions;

public class PlaygroundActivity extends AppCompatActivity implements View.OnClickListener {

    //Controls
    Button btnQuetAndAnsw, btnImgExplorer, btnReturn;
    TextView tvKidsName, tvKidsLevel,tvKidsPoints;
    ImageView kidsAvatar;
    MediaPlayer mediaPlayerPlayground;

    //Variables
    String profileID;

    //Objects
    private FirebaseAuth mAuth;
    DatabaseReference octoDB, currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playground);
        initialize();
    }

    private void initialize() {
        btnImgExplorer  = findViewById(R.id.btnImageExplorer);
        btnQuetAndAnsw  = findViewById(R.id.btnQuestionsAnswers);
        btnReturn       = findViewById(R.id.btnReturn);
        tvKidsName      = findViewById(R.id.tvKidsName);
        tvKidsLevel     = findViewById(R.id.tvKidsLevel);
        tvKidsPoints    = findViewById(R.id.tvKidsPoints);
        kidsAvatar      = findViewById(R.id.imgKidsAvatar);

        btnQuetAndAnsw.setOnClickListener(this);
        btnImgExplorer.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        octoDB  = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        profileID = "-N-SoXAIUen4myDe2rN9"; //CHANGE FOR INTENT LATER!!!
        currentProfile = octoDB.child(mAuth.getUid()).child("profiles").child(profileID);
        currentProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                tvKidsName.setText(profile.getNickName());
                tvKidsLevel.setText(String.valueOf(profile.getAge()));
                tvKidsPoints.setText(String.valueOf(profile.getPoints()));
                //kidsAvatar.setImageDrawable(profile.getAvatarUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mediaPlayerPlayground = MediaPlayer.create(this,R.raw.calm_music);
        mediaPlayerPlayground.setLooping(true);
        mediaPlayerPlayground.start();
    }

    @Override
    public void onClick(View view) {
        MediaPlayer mp;
        switch (view.getId()){
            case R.id.btnQuestionsAnswers:
                goToQuestionsAndAnswers();
                mediaPlayerPlayground.stop();
                break;
            case R.id.btnImageExplorer:
                goToImageExplorer();
                mediaPlayerPlayground.stop();
                break;
            case R.id.btnReturn:
                mp = MediaPlayer.create(this,R.raw.return_zip);
                mp.start();
                mediaPlayerPlayground.stop();
                backToMain();
                break;
        }
    }

    private void goToImageExplorer() {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra("activityType","imageExplorer");
        startActivity(intent);
        finish();
    }

    private void goToQuestionsAndAnswers() {
        Intent intent = new Intent(this, TransitionActivity.class);
        intent.putExtra("activityType","questionsAnswers");
        startActivity(intent);
        finish();
    }

    private void backToMain() {
        finish();
    }

}