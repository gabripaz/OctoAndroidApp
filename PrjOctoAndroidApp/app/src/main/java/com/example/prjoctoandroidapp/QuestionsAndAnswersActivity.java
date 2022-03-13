package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Question;

public class QuestionsAndAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    //Controls
    ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, getImgBtnAnswerFour;
    Button btnExit, btnSkip;
    TextView tvKidsName, tvQuestion, tvQuestionNumber;

    //Objects
    private FirebaseAuth mAuth;
    DatabaseReference octoDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_and_answers);
        initialize();
    }

    private void initialize() {
        imgBtnAnswerOne     =  findViewById(R.id.imgBtnAnswerOne);
        imgBtnAnswerTwo     =  findViewById(R.id.imgBtnAnswerTwo);
        imgBtnAnswerThree   =  findViewById(R.id.imgBtnAnswerThree);
        getImgBtnAnswerFour =  findViewById(R.id.imgBtnAnswerFour);

        btnExit             =  findViewById(R.id.btnExit);
        btnSkip             =  findViewById(R.id.btnSkip);

        tvKidsName          =  findViewById(R.id.tvKidsName);
        tvQuestion          =  findViewById(R.id.tvQuestion);
        tvQuestionNumber    =  findViewById(R.id.tvQuestionNumber);

        imgBtnAnswerOne.setOnClickListener(this);
        imgBtnAnswerTwo.setOnClickListener(this);
        imgBtnAnswerThree.setOnClickListener(this);
        getImgBtnAnswerFour.setOnClickListener(this);

        btnExit.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        ArrayList<Question> listOfQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("newRun");

        //MORE TESTING
        octoDB  = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference user = octoDB.child(mAuth.getUid()).child("username");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String aName = snapshot.getValue().toString();
                tvKidsName.setText(aName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //tvKidsName.setText(octoDB.child(mAuth.getUid()).child("username").get.toString());
        //END OF TESTING
    }

    //Next steps:
    /*
    * 1 - Pass user age in the intent.
    * 2 - Get another question (based on age).
    * 3 - Fill the options.
    * 4 - Check the answer.
    * 5 - Go to the next Activity (next question, skipped question or result).
    * */

    @Override
    public void onClick(View view) {

    }
}