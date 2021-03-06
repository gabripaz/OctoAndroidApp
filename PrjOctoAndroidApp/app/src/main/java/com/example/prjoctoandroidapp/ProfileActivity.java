package com.example.prjoctoandroidapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Profile;
import model.ProfileAdapter;

public class ProfileActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private CircleImageView circleImageView;
    private TextView tvNickname;
    private FloatingActionButton btnAdd;


    DatabaseReference octoDatabase;
    FirebaseStorage storage;
    StorageReference storageReference, sRef;
    ActivityResultLauncher activityResultLauncher;
    Uri filePath;
    ProgressDialog progressDialog;

    MediaPlayer mp;

    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

    //private UUID userid = FirebaseDatabase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialize();
    }

    private void initialize() {
        circleImageView = findViewById(R.id.imgDefault);
        recyclerView = findViewById(R.id.rvProfiles);
        tvNickname = findViewById(R.id.tvUsername);
        btnAdd = findViewById(R.id.btnAdd);
/*
        circleImageView.setOnClickListener(this);*/

        mp = MediaPlayer.create(this,R.raw.kid_who_r_u);
        mp.start();
        MediaPlayer mediaPlayerMain = MediaPlayer.create(this,R.raw.back_music_good);
        mediaPlayerMain.setVolume(20,20);
        mediaPlayerMain.setLooping(true);
      //  mediaPlayerMain.start();
        octoDatabase = FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            getPhoto(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        FirebaseRecyclerOptions<Profile> options =
                new FirebaseRecyclerOptions.Builder<Profile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users").child(user).child("profiles"), Profile.class)
                        .build();

        profileAdapter = new ProfileAdapter(options);
        profileAdapter.startListening();
        recyclerView.setAdapter(profileAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPlayerActivity.class));
             //   mediaPlayerMain.stop();
            }
        });
    }

    private void getPhoto(ActivityResult result) {
        if(result.getResultCode()==RESULT_OK && result.getData()!= null) {
            filePath = result.getData().getData();
            Bitmap bitmapPhoto = null;
            try {
                bitmapPhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                circleImageView.setImageBitmap(bitmapPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        profileAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queury) {
                txtSearch(queury);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void txtSearch(String str) {
        FirebaseRecyclerOptions<Profile> options =
                new FirebaseRecyclerOptions.Builder<Profile>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("nickName").startAt(str).endAt(str+"~"), Profile.class)
                        .build();
        profileAdapter = new ProfileAdapter(options);
        profileAdapter.startListening();
        recyclerView.setAdapter(profileAdapter);
    }


}