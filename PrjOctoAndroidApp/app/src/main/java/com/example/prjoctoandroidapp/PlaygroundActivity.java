package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaygroundActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnQuetAndAnsw, btnImgExplorer, btnReturn;
    TextView tvKidsName, tvKidsLevel,tvKidsPoints;
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

        btnQuetAndAnsw.setOnClickListener(this);
        btnImgExplorer.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnQuestionsAnswers:
                break;
            case R.id.btnImageExplorer:
                break;
            case R.id.btnReturn:
                backToMain();
                break;
        }
    }

    private void backToMain() {
        finish();
    }

}