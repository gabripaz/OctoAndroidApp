package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.EnumStatus;
import model.Profile;
import model.Question;
import model.RunOfQuestions;

public class QuestionsAndAnswersActivity extends AppCompatActivity implements View.OnClickListener {

    private final int MAX_NB_QUESTIONS = 5;
    private int attempt, curQuestionIndex;

    //Controls
    private ImageButton imgBtnAnswerOne, imgBtnAnswerTwo,imgBtnAnswerThree, imgBtnAnswerFour;
    private Button btnExit, btnSkip;
    private TextView tvKidsName, tvQuestion, tvQuestionNumber;

    //Objects
    private FirebaseAuth mAuth; //get the current user
    private DatabaseReference octoDB; //reference to our Database
    private DatabaseReference profileReference; //reference to the current profile.
    private Question question; // the current question
    private Profile profile; //current profile
    private MediaPlayer mediaPlayerResult, mediaPlayerBackMusic;
    private RunOfQuestions currentRun;
    private ArrayList<Integer> skippedQuestions;

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
        mediaPlayerBackMusic = MediaPlayer.create(this,R.raw.playground_music);
        mediaPlayerBackMusic.setLooping(true);
        mediaPlayerBackMusic.start();
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
        curQuestionIndex = 0;
        skippedQuestions = new ArrayList<>();
        octoDB  = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();
        String profileID = getIntent().getStringExtra("profileID");
        profileReference = octoDB.child(mAuth.getUid()).child("profiles").child(profileID); //Later we need to get the profile name.

        //String tempProfileID = "A4CtVkqMegTfIg1uow5NP6g40P92";// Delete!!! Did this because we dont login for test
        //DatabaseReference profileReference = octoDB.child(tempProfileID).child("profiles").child("0"); //Delete!!!

        profileReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profile = snapshot.getValue(Profile.class);
                tvKidsName.setText(profile.getNickName());
                currentRun = new RunOfQuestions(profile.getLastFreeRunIndex());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        loadAllQuestions();
    }

    private void loadAllQuestions() {
        DatabaseReference ques = FirebaseDatabase.getInstance().getReference("questions");
        ques.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> listRandomIndexes = new ArrayList<>();
                for (Integer i = 1; i < snapshot.getChildrenCount(); i++) {
                    listRandomIndexes.add(i);
                }
                Collections.shuffle(listRandomIndexes);
                for (Integer i = 0; i < MAX_NB_QUESTIONS; i++){
                    Integer index = listRandomIndexes.get(i);
                    DataSnapshot aQuestion = snapshot.child(index.toString());
                    Question oneQuestion = aQuestion.getValue(Question.class);
                    oneQuestion.setImages((ArrayList<String>) aQuestion.child("media").child("images").getValue());
                    if(oneQuestion.getMinage() <= profile.getAge())
                        currentRun.getListOfQuestions().add(oneQuestion);
                }

                goToNextQuestion();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    /**
     * Go to the next question and fill widgets if the current question index is not
     *  the same as the List of question size. If its equal, check the skipped ones.
     */
    private void goToNextQuestion() {
        if(curQuestionIndex >= currentRun.getListOfQuestions().size()){
            goToSkippedQuestions();
            return;
        }
        attempt = 0;
        question = currentRun.getListOfQuestions().get(curQuestionIndex);
        fillImagesInButtons();
        tvQuestion.setText(question.getStatement());
        curQuestionIndex++; //displaying is different from index.
        tvQuestionNumber.setText("Question "+ curQuestionIndex +" of " + MAX_NB_QUESTIONS);
    }

    /**
     * Go to the next skipped question and then removes the index of skipped from the list.
     * Else finish the run.
     */
    private void goToSkippedQuestions() {
        if(skippedQuestions.isEmpty()){
            displayResult();
            return;
        }
        attempt = 0;
        question = currentRun.getListOfQuestions().get(skippedQuestions.get(0));
        fillImagesInButtons();
        tvQuestion.setText(question.getStatement());
        curQuestionIndex++; //displaying is different from index.
        tvQuestionNumber.setText("Question (Skipped) "+ curQuestionIndex +" of " + MAX_NB_QUESTIONS);
        skippedQuestions.remove(0);
    }

    /**
     * Update the result and display it in another intent.
     */
    private void displayResult() {
        currentRun.setStatus(EnumStatus.FINISHED);
        SaveResult();
        Intent intent = new Intent(this,ProgressActivity.class);
        intent.putExtra("TotalPoints",currentRun.getTotalPoints());
        startActivity(intent);
        mediaPlayerBackMusic.stop();
        finish();
    }

    private void SaveResult() {
        Map<String, Object> run = new HashMap<>();
        run.put("id", String.valueOf(currentRun.getId()));
        run.put("points", currentRun.getTotalPoints());
        run.put("status", currentRun.getStatus());
        run.put("listOfQuestions", currentRun.getListofQuestionsIds());
        //run.put("date", String.valueOf(currentRun.TodaysDate()));
        profileReference.child("runs").child(String.valueOf(currentRun.getId())).setValue(run);
    }

    /**
     * Inflate the images inside the images options from the url
     */
    private void fillImagesInButtons() {
        ArrayList<ImageButton> groupOfImageButtons = new ArrayList<>(
                Arrays.asList(imgBtnAnswerOne,imgBtnAnswerTwo,imgBtnAnswerThree,imgBtnAnswerFour));
        for (int i = 1; i <= 4 ; i++) {
            try{
                Picasso.get().load(question.getImages().get(i)).fit().into(groupOfImageButtons.get(i-1));
            }catch (Exception e){
                Log.e(null, e.getMessage());
            }
        }
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
            case R.id.btnExit: //NEED TO IMPLEMENT CONFIRMATION BEFORE QUIT
                mediaPlayerBackMusic.stop();
                currentRun.setStatus(EnumStatus.ABORTED);
                finish();
               return;
            case R.id.btnSkip:
                skipQuestion();
                return;
            default:
               Toast.makeText(this, "Invalid selection", Toast.LENGTH_SHORT).show();
               return;
        }

        //Display dialog box of result.
        if(result == true){
           showAlertDialog(R.layout.dialog_postive_layout);
           mediaPlayerResult = MediaPlayer.create(this,R.raw.kid_bravo);
           currentRun.setTotalPoints((int) (currentRun.getTotalPoints() + question.getPoints()));
           goToNextQuestion();
        }else{
            showAlertDialog(R.layout.dialog_negative_layout);
            mediaPlayerResult = MediaPlayer.create(this,R.raw.kid_mocking_laugh);
        }
        if(mediaPlayerResult != null) mediaPlayerResult.start(); //crashing in my emulator, please uncomment
    }

    private void skipQuestion() {
        skippedQuestions.add(curQuestionIndex);
        goToNextQuestion();
    }

    private void showAlertDialog(int layout){
        AlertDialog.Builder dialogBuilder
                = new AlertDialog
                .Builder(this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if(mediaPlayerResult != null) mediaPlayerResult.stop();
                if(attempt == 2)  goToNextQuestion();
            }
        });

    }
    private boolean checkAns(String questionAnswer) {
        attempt++;
        if(attempt <= 2 && question.getAnswer().compareTo(String.valueOf(questionAnswer))==0){
            attempt = 0;
            return true;
        }
        return false;
    }
}