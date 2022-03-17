package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Question;

public class QuestionsAndAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    final int MAX_NB_QUESTIONS = 5;
    int attempt;

    //Controls
    ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, getImgBtnAnswerFour;
    Button btnExit, btnSkip;
    TextView tvKidsName, tvQuestion, tvQuestionNumber;


    //Objects
    private FirebaseAuth mAuth;
    DatabaseReference octoDB;
    Question question;

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
        attempt=0;
        ArrayList<Question> listOfQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("newRun");

        //MORE TESTING
        octoDB  = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference user = octoDB.child(mAuth.getUid()).child("username"); //Later we need to get the profile name.
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
        int curQNb = listOfQuestions.size();
        tvQuestionNumber.setText("Question"+ curQNb +" of " + MAX_NB_QUESTIONS);
        getQuestion("1");

    }

    private void getQuestion(String questionID) {

        DatabaseReference ques = FirebaseDatabase.getInstance().getReference("questions").child(questionID);
        ques.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question = new Question();

                for(DataSnapshot snap : snapshot.getChildren()){

                    if(snap.getKey().compareTo("answer") == 0){
                        question.setAnswer(snap.getValue(String.class));
                    }
                    if(snap.getKey().compareTo("minage") == 0){
                        long age = snap.getValue(Long.class);
                        question.setMinage(age);
                    }
                    if(snap.getKey().compareTo("options") == 0){
                        ArrayList<String> opt = new ArrayList<>();
                        for (DataSnapshot s : snap.getChildren()){
                            long option = s.getValue(Long.class);
                            opt.add(String.valueOf(option));
                        }
                        question.setOptions(opt);
                    }

                    if(snap.getKey().compareTo("points") == 0){
                        long point = snap.getValue(Long.class);
                        question.setPoints(point);
                    }

                     if(snap.getKey().compareTo("statement") == 0){
                        question.setStatement(snap.getValue(String.class));
                        tvQuestion.setText(question.getStatement());
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
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
        attempt++;
        boolean result=false;
        switch(view.getId()){
            case R.id.imgBtnAnswerOne:
               result= checkAns(2,attempt);
               break;
            case R.id.imgBtnAnswerTwo:
               result= checkAns(3,attempt);
               break;
            case R.id.imgBtnAnswerThree:
               result= checkAns(100,attempt);
               break;
            case R.id.imgBtnAnswerFour:
               result= checkAns(51,attempt);
               break;
            case R.id.btnExit: //NEED TO IMPLEMENT CONFIRMATION
               finish();
               break;
            default:
               Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
               break;
        }
        Toast.makeText(this, "Result is:"+result, Toast.LENGTH_SHORT).show();
        if(result == true){
           // Toast.makeText(this, "Congratulations...", Toast.LENGTH_SHORT).show();
            showAlertDialog(R.layout.dialog_postive_layout);
        }else{
            //Toast.makeText(this, "Sorry You have Excedd attemps", Toast.LENGTH_SHORT).show();
            showAlertDialog(R.layout.dialog_negative_layout);
        }
    }

    private void showAlertDialog(int layout){
        //AlertDialog dialogBuilder = new AlertDialog.Builder(this);
        AlertDialog.Builder dialogBuilder
                = new AlertDialog
                .Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        dialogBuilder.setView(layoutView);
        //alertDialog = dialogBuilder.create();
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    private boolean checkAns(int i, int attempt) {
        boolean result= false;
        if(attempt >2){
            result=false;
        }else{
            if(question.getAnswer().compareTo(String.valueOf(i))==0){
                result=true;
                //Toast.makeText(this, "Ans checked", Toast.LENGTH_SHORT).show();
            }else{
                result=false;
            }
        }
        return result;
    }
}