package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.ArrayList;
import java.util.Arrays;

import model.Question;

public class QuestionsAndAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    final int MAX_NB_QUESTIONS = 5;
    int attempt;

    //Controls
    ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, imgBtnAnswerFour;
    ArrayList<ImageButton> groupOfImageButtons = new ArrayList<>(
            Arrays.asList(imgBtnAnswerOne,imgBtnAnswerTwo,imgBtnAnswerThree,imgBtnAnswerFour));
    Button btnExit, btnSkip;
    TextView tvKidsName, tvQuestion, tvQuestionNumber;


    //Objects
    private FirebaseAuth mAuth; //get the current user
    DatabaseReference octoDB; //reference to our Database
    Question question; // the current question
    ArrayList<String> imagesURLs;
    ArrayList<Question> questionsList;
    MediaPlayer mediaPlayerResult;

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
        questionsList = new ArrayList<Question>();
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        int curQNb = 1;
        tvQuestionNumber.setText("Question "+ curQNb +" of " + MAX_NB_QUESTIONS);
        getQuestion("1");
        loadAllQuestions();
    }

    private void getQuestion(String questionID) {

        DatabaseReference ques = FirebaseDatabase.getInstance().getReference("questions").child(questionID);
        ques.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                question = new Question();
                imagesURLs = (ArrayList) snapshot.child("media").child("images").getValue();
                question.setAnswer((String)snapshot.child("answer").getValue());
                question.setMinage((Long)snapshot.child("minage").getValue());
                question.setOptions((ArrayList<String>)snapshot.child("options").getValue());
                question.setPoints((Long)snapshot.child("points").getValue());
                question.setStatement((String)snapshot.child("statement").getValue());
                tvQuestion.setText(question.getStatement());
                fillImagesInButtons();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void loadAllQuestions() {

        DatabaseReference ques = FirebaseDatabase.getInstance().getReference("questions");
        ques.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot aQuestion: snapshot.getChildren())
                {
                    Question q = aQuestion.getValue(Question.class);
                    questionsList.add(q);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void fillImagesInButtons() {
        Picasso.get().load(imagesURLs.get(1)).fit().into(imgBtnAnswerOne);
        Picasso.get().load(imagesURLs.get(2)).fit().into(imgBtnAnswerTwo);
        Picasso.get().load(imagesURLs.get(3)).fit().into(imgBtnAnswerThree);
        Picasso.get().load(imagesURLs.get(4)).fit().into(imgBtnAnswerFour);
    }

    @Override
    public void onClick(View view) {
        boolean result=false;
        switch(view.getId()){
            case R.id.imgBtnAnswerOne:
               result= checkAns(String.valueOf(question.getOptions().get(1)));
               break;
            case R.id.imgBtnAnswerTwo:
                result= checkAns(String.valueOf(question.getOptions().get(2)));
               break;
            case R.id.imgBtnAnswerThree:
                result= checkAns(String.valueOf(question.getOptions().get(3)));
               break;
            case R.id.imgBtnAnswerFour:
                result= checkAns(String.valueOf(question.getOptions().get(4)));
               break;
            case R.id.btnExit: //NEED TO IMPLEMENT CONFIRMATION
               finish();
               return;
            default:
               Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
               return;
        }

        Toast.makeText(this, "Result is:"+result, Toast.LENGTH_SHORT).show();

        if(result == true){
           showAlertDialog(R.layout.dialog_postive_layout);
           mediaPlayerResult = MediaPlayer.create(this,R.raw.right_answer_applause);
           mediaPlayerResult.start();
        }else{
            showAlertDialog(R.layout.dialog_negative_layout);
            mediaPlayerResult = MediaPlayer.create(this,R.raw.wrong_ans_crow);
            mediaPlayerResult.start();
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
                alertDialog.dismiss(); mediaPlayerResult.stop();
            }
        });
    }
    private boolean checkAns(String questionAnswer) {
        attempt++;
        if(attempt <= 2 && question.getAnswer().compareTo(String.valueOf(questionAnswer))==0){
            return true;
        }
        return false;
    }
}