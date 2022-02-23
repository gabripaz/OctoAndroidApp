package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionsAndAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, getImgBtnAnswerFour;

    Button btnExit, btnSkip;

    TextView tvKidsName, tvQuestion, tvQuestionNumber;
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

    }

    @Override
    public void onClick(View view) {

    }
}