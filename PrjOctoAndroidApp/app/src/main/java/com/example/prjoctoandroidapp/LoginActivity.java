package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Speaker;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edUsernameEmail, edPassword;
    Button btnLogin, btnReturn;

    DatabaseReference octoDB;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize() {
        edUsernameEmail = findViewById(R.id.edUsernameEmail);
        edPassword      = findViewById(R.id.edPassword);
        btnLogin        = findViewById(R.id.btnLog);
        btnReturn       = findViewById(R.id.btnReturnLg);

        btnReturn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        octoDB  = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progressBar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReturnLg:
                goBack();
                break;
            case R.id.btnLog:
                loginIntoAcc();
                break;
        }
    }

    private void loginIntoAcc() {
        String email, password;

        email    =  edUsernameEmail.getText().toString();
        password =  edPassword.getText().toString();
        //verifying that passwrd and email are not empty
        if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       sentToMainActivity();
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error "+error,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //TO BE REMOVED!!!!!!!!////////////////////
        Speaker.SpeakThis(this,email);   //TESTING REMOVE IF ITS WORKING
        Speaker.Destroy();
        ///////////////////////////////////////////
//
    }

    private void sentToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goBack() {
        finish();
    }

    //TESTING FIREBASE AUTHENTICATION I might remove this later
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currUser != null){
            Toast.makeText(this,"No users register yet", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }
    }
    //END TEST
}