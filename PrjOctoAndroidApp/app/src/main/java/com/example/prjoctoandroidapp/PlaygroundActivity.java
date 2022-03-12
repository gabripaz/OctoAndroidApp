package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

import model.Question;
import model.RunOfQuestions;

public class PlaygroundActivity extends AppCompatActivity implements View.OnClickListener {

    //Controls
    Button btnQuetAndAnsw, btnImgExplorer, btnReturn;
    TextView tvKidsName, tvKidsLevel,tvKidsPoints;
    ImageView kidsAvatar;

    //Variables
    int profileID;

    //Objects
    private FirebaseAuth mAuth;
    DatabaseReference octoDB,currentProfile;

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

        octoDB  = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        //We need to get the profile ID to go further.
        //currentProfile = octoDB.child(mAuth.getUid()).child("profile").child(profileID);

    }

    @Override
    public void onClick(View view) {
        MediaPlayer mp;
        switch (view.getId()){
            case R.id.btnQuestionsAnswers:
                mp = MediaPlayer.create(this,R.raw.trill_c);
                mp.start();
                goToQuestionsAndAnswers();
                break;
            case R.id.btnImageExplorer:
                mp = MediaPlayer.create(this,R.raw.trill_f);
                mp.start();
                goToImageExplorer();
                break;
            case R.id.btnReturn:
                mp = MediaPlayer.create(this,R.raw.return_zip);
                mp.start();
                backToMain();
                break;
        }
    }

    private void goToImageExplorer() {
        Intent intent = new Intent(this, MLTestActivity.class);
        startActivity(intent);
    }

    private void goToQuestionsAndAnswers() {
        Intent intent = new Intent(this, QuestionsAndAnswersActivity.class);
        intent.putExtra("newRun", new ArrayList<Question>());
        startActivity(intent);
    }

    private void backToMain() {
        finish();
    }

}