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
                       sentToUserAcc();
                    }else{
                        String error = task.getException().getMessage();
                        Toast.makeText(getApplicationContext(),"Error "+error+"\n  there is not users with that credentials",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        //TO BE REMOVED!!!!!!!!////////////////////
        Speaker.SpeakThis(this,email);   //TESTING REMOVE IF ITS WORKING
        Speaker.Destroy();
        ///////////////////////////////////////////
//        DatabaseReference userChild;
//        userChild = octoDB.child(email);
//        userChild.addValueEventListener(this);
    }

    private void sentToUserAcc() {
        Intent intent = new Intent(this,UserAccountActivity.class);
        intent.putExtra("currentUser", mAuth.getCurrentUser().toString());
        startActivity(intent);
        finish();
    }

    private void goBack() {
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
//        String usernameEmail, password, dbPassword, fullName,email;
//        usernameEmail = edUsernameEmail.getText().toString();
//        if(snapshot.exists()){
//            password=edPassword.getText().toString();
//            dbPassword = snapshot.child("password").getValue().toString();
//            if(password.compareTo(dbPassword)==0){
//                fullName = snapshot.child("fullname").getValue().toString();
//                email = snapshot.child("email").getValue().toString();
//                OctoUser currUser = new OctoUser(usernameEmail, fullName,email,password);
//                Intent intent = new Intent(this, UserAccountActivity.class);
//                intent.putExtra("currentUser", currUser);
//                startActivity(intent);
//
//            }else{
//                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
//                edPassword.setText(null);
//            }
//        }else{
//            Toast.makeText(this,"The username "+usernameEmail+" does not exist. Verify or create an account", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
    //TESTING FIREBASE AUTHENTICATION
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
}