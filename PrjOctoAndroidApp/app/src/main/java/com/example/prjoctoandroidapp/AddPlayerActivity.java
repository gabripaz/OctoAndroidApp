package com.example.prjoctoandroidapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import model.Reward;

public class AddPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edNickname, edAge;
    Button btnReturn, btnAddPlayer;
    ImageButton btnAvatarOne,btnAvatarTwo,btnAvatarThree,btnAvatarFour,btnAvatarFive,btnAvatarSix;

    DatabaseReference octoDatabase;
    //To upload the image file into Firebase database
    FirebaseStorage storage;
    StorageReference storageReference,sRef;

    // to declare activity result launcher to call and receive
    ActivityResultLauncher activityResultLauncher;

    Uri filePath;

    // follow the photo upload
    ProgressDialog progressDialog;

    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        btnReturn.setOnClickListener(this);
        btnAddPlayer.setOnClickListener(this);

        octoDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
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
            case R.id.btnBrowse2:
                break;
        }
    }

    private void addPlayer() {
        String UID = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            UID = user.getUid();
        } else {
            Toast.makeText(this, "There seems to be issues with the current session", Toast.LENGTH_LONG).show();
        }

        filePath = Uri.parse("https://firebasestorage.googleapis.com/v0/b/octodatabase-1e2c1.appspot.com/o/avatar_six.png?alt=media&token=6e61b36e-62ef-4817-ad85-bf55b4986898");

        Map<String, Object> map = new HashMap<>();
        map.put("profileID", "");
        map.put("nickName", edNickname.getText().toString());
        map.put("age", Integer.parseInt(edAge.getText().toString()));
        map.put("points", Integer.parseInt("0"));
        map.put("avatarUrl", filePath.toString()); //please insert the selected avatar
        Reward reward = new Reward("welcome",0);
        map.put("rewards", new ArrayList<Reward>(Arrays.asList(reward)));

        FirebaseDatabase.getInstance().getReference("users").child(UID).child("profiles").push().setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddPlayerActivity.this, "Data Saved Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddPlayerActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(AddPlayerActivity.this, "Error during insertion", Toast.LENGTH_LONG).show();
                    }
                });

        //THIS CODE IS ONLY FOR DEMONSTRATION PROPOSES TO DELETE OR MODIFY LATER
        Toast.makeText(this,"Profile for "+edNickname.getText().toString()+" was added", Toast.LENGTH_LONG).show();
        //
    }
}