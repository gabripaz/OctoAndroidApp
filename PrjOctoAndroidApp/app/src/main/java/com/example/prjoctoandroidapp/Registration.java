package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import java.util.regex.Pattern;

import model.OctoUser;
import model.Profile;
import model.Reward;


public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText edUsername,edFullName, edEmail, edPassword, edRepPassword;
    TextView tvError;
    Button btnCreate,btnReturn;
    ProgressBar progressBar;

    DatabaseReference octoDB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialized();
    }

    private void initialized() {
        edUsername    =  findViewById(R.id.edUsername);
        edFullName    =  findViewById(R.id.edFullName);
        edEmail       =  findViewById(R.id.edEmail);
        edPassword    =  findViewById(R.id.edPassword);
        edRepPassword =  findViewById(R.id.edRepPass);
        tvError       =  findViewById(R.id.tvError);
        btnCreate     =  findViewById(R.id.btnCreateAcc);
        btnReturn     =  findViewById(R.id.btnReturn);
        progressBar   =  findViewById(R.id.progressBar_reg);

        btnCreate.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        octoDB  = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        MediaPlayer mp;
        switch (view.getId()){
            case R.id.btnReturn:
                mp = MediaPlayer.create(this,R.raw.return_zip);
                mp.setVolume(60,60);
                mp.start();
                goBack();
                break;
            case R.id.btnCreateAcc:
                mp = MediaPlayer.create(this,R.raw.register_launch);
                mp.setVolume(60,60);
                mp.start();
                createAccount();
                break;
        }
    }

    private void createAccount() {
      if( !validateUsername() || !validateFullname()  || !validateEmail() || !validatePassword()){return;}
//
        String  fullName, username, email,password;

        username = edUsername.getText().toString();
        fullName = edFullName.getText().toString();
        email    = edEmail.getText().toString();
        password = edPassword.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        //FIREBASE AUTH
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Inserting the rest of the data in the database
                    String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                    Reward reward = new Reward("welcome",0);
                    Profile profile = new Profile("Guest","",0,new ArrayList<Reward>(Arrays.asList(reward)));

                    OctoUser user = new OctoUser(username,fullName,email, new ArrayList<Profile>(Arrays.asList(profile)));
                    octoDB.child(uid).setValue(user);
                    mAuth.signOut();
                    sendToMain();
                }else{
                    Toast.makeText(getApplicationContext(),"Error "+task.getException().toString()+" \n account no created", Toast.LENGTH_LONG).show();
                }

            }
        });
        //FIREBASE AUTH

    }
    private void goBack() {
        finish();
    }

    //FIREBASE AUTHENTICATION
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currUser != null){
            //sentToMain();
            Toast.makeText(this,"No users register yet", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateUsername(){
        int usernameLg;
        usernameLg = edUsername.getText().toString().trim().length();
         if(usernameLg==0){
            // Toast.makeText(this,"Username is required",Toast.LENGTH_SHORT).show();
             tvError.setText(tvError.getText() + "Username is required\n");
             edUsername.requestFocus();
             return false;
         }else{
             tvError.setText(null);
             return true;
         }

    }
    private boolean validateFullname(){
        int fullnameLg;
        fullnameLg = edFullName.getText().toString().trim().length();
        if(fullnameLg==0){
           // Toast.makeText(this,"The name is required",Toast.LENGTH_SHORT).show();
            tvError.setText(tvError.getText() + "Name is required\n");
            edFullName.requestFocus();
            return false;
        }else{
            tvError.setText(null);
            return true;
        }

    }

    private boolean validateEmail() {
        try {
            String email;
            Boolean matchRegex;

            email    = edEmail.getText().toString().trim();

            matchRegex  = Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);

            if(matchRegex){
                //Toast.makeText(this,"Email OK",Toast.LENGTH_SHORT).show();
                tvError.setText(null);
                return true;
            }else{
               // Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show();
                tvError.setText(tvError.getText() + "Invalid Email format\n");
                edEmail.requestFocus();
                return false;
            }

        }catch (Exception e){

            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    private boolean validatePassword() {
        try {
            String password, repPassword;
            int matchPass;
            password    = edPassword.getText().toString().trim();
            repPassword = edRepPassword.getText().toString().trim();
            matchPass   = password.compareTo(repPassword);

            boolean matchRegex;


            matchRegex  = Pattern.matches("^[A-Z](?=.*[a-z])(?=.*[0-9]).{6,10}$", password);

            if(!matchRegex){

                //Toast.makeText(this,"Password does no met the requirements",Toast.LENGTH_SHORT).show();
                tvError.setText(tvError.getText() + "Password does no met the requirements\nFirst letter uppercase\nNeeds a number\nBetween 6 and 10 characters\n");
                edPassword.requestFocus();
                return false;

            }else if(matchPass!=0){

               // Toast.makeText(this,"Password does no match",Toast.LENGTH_SHORT).show();
               tvError.setText(tvError.getText() + "Password does no match\n");
               edRepPassword.setText(null);
               return  false;

            }
            else{
               // Toast.makeText(this,"Password OK",Toast.LENGTH_SHORT).show();
                tvError.setText(null);
                return true;

            }

        }catch (Exception e){

            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;

        }

    }




}