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
    ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, imgBtnAnswerFour;
    Button btnExit, btnSkip;
    TextView tvKidsName, tvQuestion, tvQuestionNumber;


    //Objects
    private FirebaseAuth mAuth;
    DatabaseReference octoDB;
    Question question;
    ArrayList<String> imagesURLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_and_answers);
        initialize();
    }

    private void initialize() {
        //Controls
        imgBtnAnswerOne     =  findViewById(R.id.imgBtnAnswerOne);
        imgBtnAnswerTwo     =  findViewById(R.id.imgBtnAnswerTwo);
        imgBtnAnswerThree   =  findViewById(R.id.imgBtnAnswerThree);
        imgBtnAnswerFour =  findViewById(R.id.imgBtnAnswerFour);

        btnExit             =  findViewById(R.id.btnExit);
        btnSkip             =  findViewById(R.id.btnSkip);

        tvKidsName          =  findViewById(R.id.tvKidsName);
        tvQuestion          =  findViewById(R.id.tvQuestion);
        tvQuestionNumber    =  findViewById(R.id.tvQuestionNumber);

        imgBtnAnswerOne.setOnClickListener(this);
        imgBtnAnswerTwo.setOnClickListener(this);
        imgBtnAnswerThree.setOnClickListener(this);
        imgBtnAnswerFour.setOnClickListener(this);

        btnExit.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        //Variables and objects
        attempt=0;
        ArrayList<Question> listOfQuestions = (ArrayList<Question>) getIntent().getSerializableExtra("newRun");

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
                imagesURLs = new ArrayList<>();
                imagesURLs = (ArrayList) snapshot.child("media").child("images").getValue();
                question.setAnswer((String)snapshot.child("answer").getValue());
                question.setMinage((Long)snapshot.child("minage").getValue());
                question.setOptions((ArrayList)snapshot.child("options").getValue());
                question.setPoints((Long)snapshot.child("points").getValue());
                question.setStatement((String)snapshot.child("statement").getValue());
                tvQuestion.setText(question.getStatement());
                //Picasso.with(this).load(photoURl).placeholder().into();
                //his video
                //https://www.youtube.com/watch?v=UDZpfYf-E7A&t=158s

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
           showAlertDialog(R.layout.dialog_postive_layout);
        }else{
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
        if(attempt <= 2 && question.getAnswer().compareTo(String.valueOf(i))==0){
            return true;
        }
        return false;
    }
}