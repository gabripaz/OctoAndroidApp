package com.example.prjoctoandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Ringtone;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AddPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edNickname, edAge;
    Button btnReturn, btnAddPlayer;
    ImageButton btnAvatarOne,btnAvatarTwo,btnAvatarThree,btnAvatarFour,btnAvatarFive,btnAvatarSix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        initialize();
    }

    private void initialize() {
        edNickname     =   findViewById(R.id.edNickname);
        edAge          =   findViewById(R.id.edAge);
        btnAddPlayer   =   findViewById(R.id.btnNewPlayer);
        btnReturn      =   findViewById(R.id.btnReturnAddPlayer);
        btnAvatarOne   =   findViewById(R.id.imgAvatarOne);
        btnAvatarTwo   =   findViewById(R.id.imgAvatarTwo);
        btnAvatarThree =   findViewById(R.id.imgAvatarThree);
        btnAvatarFour  =   findViewById(R.id.imgAvatarFour);
        btnAvatarFive  =   findViewById(R.id.imgAvatarFive);
        btnAvatarSix   =   findViewById(R.id.imgAvatarSix);

        btnReturn.setOnClickListener(this);
        btnAddPlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNewPlayer:
                addPlayer();
                break;
            case R.id.btnReturnAddPlayer:
                finish();
                break;
        }
    }

    private void addPlayer() {
        //THIS CODE IS ONLY FOR DEMONSTRATION PROPOSES TO DELETE OR MODIFY LATER
        Toast.makeText(this,"Profile for "+edNickname.getText().toString()+" was added", Toast.LENGTH_LONG).show();
        //
    }
}