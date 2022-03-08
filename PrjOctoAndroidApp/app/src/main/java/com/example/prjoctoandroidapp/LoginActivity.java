package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edUserEmail, edPassword;
    Button btnLogin, btnReturn;
    TextToSpeech tts;

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
        edUserEmail = findViewById(R.id.edUserEmail);
        edPassword      = findViewById(R.id.edPassword);
        btnLogin        = findViewById(R.id.btnLog);
        btnReturn       = findViewById(R.id.btnReturnLg);

        btnReturn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        octoDB  = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.login_progressBar);

        tts = new TextToSpeech(this.getApplicationContext(),
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i == TextToSpeech.SUCCESS){
                            int res = tts.setLanguage(Locale.US);
                            switch (res){
                                case TextToSpeech.LANG_MISSING_DATA:
                                case TextToSpeech.LANG_NOT_SUPPORTED:
                                    Log.e("TTS","Language not supported");
                                    break;
                            }
                        } else{
                            Log.e("TTS","Initialization Failed");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        MediaPlayer mp;
        switch (view.getId()){
            case R.id.btnReturnLg:
                mp = MediaPlayer.create(this,R.raw.return_zip);
                mp.setVolume(60,60);
                mp.start();
                goBack();
                break;
            case R.id.btnLog:
                mp = MediaPlayer.create(this,R.raw.login_launch);
                mp.setVolume(60,60);
                mp.start();
                loginIntoAcc();
                break;
        }
    }

    private void loginIntoAcc() {
        String email, password;

        email    =  edUserEmail.getText().toString();
        password =  edPassword.getText().toString();
        //verifying that password and email are not empty
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
        /*tts.setPitch(1f);
        tts.setSpeechRate(1f);
        tts.speak("Welcome Mister or Misses " + email, TextToSpeech.QUEUE_FLUSH, null);*/
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

    //DESTROYING TEXT TO SPEECH

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
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