package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaygroundActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnQuetAndAnsw, btnImgExplorer, btnReturn;
    TextView tvKidsName, tvKidsLevel,tvKidsPoints;
    ImageView kidsAvatar;
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnQuestionsAnswers:
                goToQuestionsAndAnswers();
                break;
            case R.id.btnImageExplorer:
                break;
            case R.id.btnReturn:
                backToMain();
                break;
        }
    }

    private void goToQuestionsAndAnswers() {
        Intent intent = new Intent(this, QuestionsAndAnswersActivity.class);
        startActivity(intent);
    }

    private void backToMain() {
        finish();
    }

}