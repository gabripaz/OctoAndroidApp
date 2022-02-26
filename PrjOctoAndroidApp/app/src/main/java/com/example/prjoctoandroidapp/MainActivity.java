package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextToSpeech textToSpeech;
//    private EditText edText;
//    private Button btnSpeech;
    Button btnCreateAcc, btnLogin, btnStart, btnConfigAcc, btnLogout, btnAddPlayer;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {
            btnLogin     = findViewById(R.id.btnLogin);
            btnCreateAcc = findViewById(R.id.btnRegister);
            btnStart     = findViewById(R.id.btnStart);
            btnConfigAcc = findViewById(R.id.btnConfigAcc);
            btnLogout    = findViewById(R.id.btnLogout);
            btnAddPlayer = findViewById(R.id.btnAddPlayer);
            
            btnLogin.setOnClickListener(this);
            btnCreateAcc.setOnClickListener(this);
            btnStart.setOnClickListener(this);
            btnConfigAcc.setOnClickListener(this);
            btnLogout.setOnClickListener(this);
            btnAddPlayer.setOnClickListener(this);

            mAuth = FirebaseAuth.getInstance();
   
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnLogin):
                goLogin();
                break;
            case(R.id.btnRegister):
                goCreateAcc();
                break;
            case(R.id.btnStart):
                goStart();
                break;
            case(R.id.btnAddPlayer):
                goToAddPlayer();
            break;
            case(R.id.btnConfigAcc):
                goUserAcc();
                break;
            case(R.id.btnLogout):
                logout();
                break;
        }
    }

    private void goToAddPlayer() {
        Intent intent = new Intent(this,AddPlayerActivity.class);
        startActivity(intent);
    }

    private void logout() {
        mAuth.signOut();
        restartActivity();
    }
    private void restartActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void goUserAcc() {
        Intent intent = new Intent(this,UserAccountActivity.class);
        startActivity(intent);
    }

    private void goStart() {
        Intent intent = new Intent(this,PlaygroundActivity.class);
        startActivity(intent);
    }

    private void goCreateAcc() {
        Intent intent = new Intent(this,Registration.class);
        startActivity(intent);

    }

    private void goLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    //TESTING FIREBASE AUTHENTICATION
     @Override
    protected void onStart() {
         super.onStart();
         FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
         if(currUser == null){
             btnStart.setVisibility(View.GONE);
             btnLogout.setVisibility(View.GONE);
             btnConfigAcc.setVisibility(View.GONE);
             Toast.makeText(this,"No users login yet", Toast.LENGTH_SHORT).show();
         }
         else{
             btnStart.setVisibility(View.VISIBLE);
             btnLogout.setVisibility(View.VISIBLE);
             btnConfigAcc.setVisibility(View.VISIBLE);
             btnLogin.setVisibility(View.GONE);
             btnCreateAcc.setVisibility(View.GONE);
         }
     }


//      GABRIEL Text to speech code test
//    private void intialize() {
//        btnSpeech = findViewById(R.id.btnSpeak);
//        edText = findViewById(R.id.edTextToSay);
//        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int i) {
//                if(i == TextToSpeech.SUCCESS){ //validates the initialization
//                    int res = textToSpeech.setLanguage(Locale.ENGLISH);
//                    switch (res){
//                        case TextToSpeech.LANG_MISSING_DATA:
//                        case TextToSpeech.LANG_NOT_SUPPORTED:
//                            Log.e("Text to Speech", "Language not supported");
//                            break;
//                        default:
//                            btnSpeech.setEnabled(true);
//                    }
//                }else{
//                    Log.e("Text to Speech", "Initialization Failed");
//                }
//            }
//        });
//        btnSpeech.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                speak();
//            }
//        });
//    }
//
//    private void speak() {
//        String message = edText.getText().toString();
//        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
//    }
//    //just for security
//    @Override
//    protected void onDestroy() {
//        if (textToSpeech != null) {
//            textToSpeech.stop();
//            textToSpeech.shutdown();
//        }
//        super.onDestroy();
//    }
}