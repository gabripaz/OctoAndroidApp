package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Profile;
import model.ProfileAdapter;

public class ProfileActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter profileAdapter;
    private CircleImageView circleImageView;
    private TextView tvNickname;
    private FloatingActionButton btnAdd;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
            }
        });
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