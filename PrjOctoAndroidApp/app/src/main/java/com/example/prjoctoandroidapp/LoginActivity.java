package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import model.OctoUser;
import model.Speaker;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    EditText edUsernameEmail, edPassword;
    Button btnLogin, btnReturn;

    DatabaseReference octoDB;

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
        String username;
        username = edUsernameEmail.getText().toString();
        //TO BE REMOVED!!!!!!!!////////////////////
        Speaker.SpeakThis(this,username);   //TESTING REMOVE IF ITS WORKING
        Speaker.Destroy();
        ///////////////////////////////////////////
        DatabaseReference userChild;
        userChild = octoDB.child(username);
        userChild.addValueEventListener(this);
    }
    private void goBack() {
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String usernameEmail, password, dbPassword, fullName,email;
        usernameEmail = edUsernameEmail.getText().toString();
        if(snapshot.exists()){
            password=edPassword.getText().toString();
            dbPassword = snapshot.child("password").getValue().toString();
            if(password.compareTo(dbPassword)==0){
                fullName = snapshot.child("fullname").getValue().toString();
                email = snapshot.child("email").getValue().toString();
                OctoUser currUser = new OctoUser(usernameEmail, fullName,email,password);
                Intent intent = new Intent(this, UserAccountActivity.class);
                intent.putExtra("currentUser", currUser);
                startActivity(intent);

            }else{
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                edPassword.setText(null);
            }
        }else{
            Toast.makeText(this,"The username "+usernameEmail+" does not exist. Verify or create an account", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}