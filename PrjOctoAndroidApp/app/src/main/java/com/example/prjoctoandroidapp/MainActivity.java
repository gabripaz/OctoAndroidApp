package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextToSpeech textToSpeech;
//    private EditText edText;
//    private Button btnSpeech;
    Button btnCreateAcc, btnLogin, btnStart, btnConfigAcc, btnLogout, btnAddPlayer;
    private FirebaseAuth mAuth;
    MediaPlayer mediaPlayerMain;
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

            MainAudioStart();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.btnLogin):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerLogin = MediaPlayer.create(this,R.raw.login_waterdrop);
                mediaPlayerLogin.setVolume(60,60);
                mediaPlayerLogin.start();*/
                goLogin();
                break;
            case(R.id.btnRegister):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerRegister = MediaPlayer.create(this,R.raw.register_waterdrop);
                //mediaPlayerRegister.setVolume(60,60);
                mediaPlayerRegister.start();*/
                goCreateAcc();
                break;
            case(R.id.btnStart):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerStart = MediaPlayer.create(this,R.raw.btn_click_pop);
                mediaPlayerStart.setVolume(60,60);
                mediaPlayerStart.start();*/
                goStart();
                break;
            case(R.id.btnAddPlayer):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerAddp = MediaPlayer.create(this,R.raw.kid_who_r_u);
                mediaPlayerAddp.setVolume(60,60);
                mediaPlayerAddp.start();*/
                goToAddPlayer();
            break;
            case(R.id.btnConfigAcc):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerConfig = MediaPlayer.create(this,R.raw.btn_click_pop2);
                mediaPlayerConfig.setVolume(60,60);
                mediaPlayerConfig.start();*/
                goUserAcc();
                break;
            case(R.id.btnLogout):
                mediaPlayerMain.stop();
                /*MediaPlayer mediaPlayerLogOut = MediaPlayer.create(this,R.raw.logout_zip);
                mediaPlayerLogOut.setVolume(60,60);
                mediaPlayerLogOut.start();*/
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
        //We need to put in the intent the id from the profile to manage easily later
        //intent.putExtra("id":id)
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
         MainAudioStart();
         MediaPlayer mediaPlayerWelcome;
         FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
         if(currUser == null){
             btnStart.setVisibility(View.GONE);
             btnLogout.setVisibility(View.GONE);
             btnConfigAcc.setVisibility(View.GONE);
             btnAddPlayer.setVisibility(View.GONE);
             Toast.makeText(this,"No users login yet", Toast.LENGTH_SHORT).show();
             mediaPlayerWelcome = MediaPlayer.create(this,R.raw.kid_welcome);
             mediaPlayerWelcome.start();
         }
         else{
             btnStart.setVisibility(View.VISIBLE);
             btnLogout.setVisibility(View.VISIBLE);
             btnConfigAcc.setVisibility(View.VISIBLE);
             btnAddPlayer.setVisibility(View.VISIBLE);
             btnLogin.setVisibility(View.GONE);
             btnCreateAcc.setVisibility(View.GONE);
             mediaPlayerWelcome = MediaPlayer.create(this,R.raw.kid_welcome_back);
             mediaPlayerWelcome.start();
         }
     }

    private void MainAudioStart() {
        mediaPlayerMain = MediaPlayer.create(this, R.raw.main_ocean);
        mediaPlayerMain.setLooping(true);
        mediaPlayerMain.start();
    }
}