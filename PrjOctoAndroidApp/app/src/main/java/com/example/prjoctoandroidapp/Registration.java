package com.example.prjoctoandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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



import java.util.UUID;
import java.util.regex.Pattern;

import model.OctoUser;


public class Registration extends AppCompatActivity implements View.OnClickListener {
    EditText edUsername,edFullName, edEmail, edPassword, edRepPassword;
    Button btnCreate,btnReturn;
    ProgressBar progressBar;

    private static final String TAG = "EmailPassword";
    DatabaseReference octoDB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initialized();
    }

    private void initialized() {
        edUsername    =  findViewById(R.id.edUsername);
        edFullName    =  findViewById(R.id.edFullName);
        edEmail       =  findViewById(R.id.edEmail);
        edPassword    =  findViewById(R.id.edPassword);
        edRepPassword =  findViewById(R.id.edRepPass);
        btnCreate     =  findViewById(R.id.btnCreateAcc);
        btnReturn     =  findViewById(R.id.btnReturn);
        progressBar   =  findViewById(R.id.progressBar_reg);

        btnCreate.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        edFullName.addTextChangedListener(new ValidatorTextWatcher(edFullName));
        edUsername.addTextChangedListener(new ValidatorTextWatcher(edUsername));
        edPassword.addTextChangedListener(new ValidatorTextWatcher(edPassword));
        edEmail.addTextChangedListener(new ValidatorTextWatcher(edEmail));

        octoDB  = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnReturn:
                goBack();
                break;
            case R.id.btnCreateAcc:
                createAccount();
                break;
        }
    }

    private void createAccount() {
      if(!validateEmail()){return;}
      if(!validatePassword()){return;}
      if(!validateFullname()){return;}
      if(!validateUsername()){return;}
        String uniqueID, fullname, username, email,password;

        uniqueID = UUID.randomUUID().toString();
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        username = edUsername.getText().toString();
        fullname = edFullName.getText().toString();
        email    = edEmail.getText().toString();
        password = edPassword.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        //TESTING FIREBASE AUTH
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sentToMain();
                }else{
                    Toast.makeText(getApplicationContext(),"Error "+task.getException().toString()+" \n account no created", Toast.LENGTH_LONG).show();
                }

            }
        });
        //END TEST

        OctoUser user = new OctoUser(uniqueID,username,fullname,email,password);

        octoDB.child(uniqueID).setValue(user);
          finish();
    }
    private void goBack() {
        finish();
    }

    //TESTING FIREBASE AUTHENTICATION
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currUser != null){
            //sentToMain();
            Toast.makeText(this,"No users register yet", Toast.LENGTH_SHORT).show();

        }
    }

    private void sentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    //END TEST


//GABRIEL TEST CODE
//    private void comparePassword(View view) {
//        try {
//            String password, repPassword;
//            int matchPass;
//            password    = edPassword.getText().toString();
//            repPassword = edRepPassword.getText().toString();
//            matchPass   = password.compareTo(repPassword);
//            if(matchPass==0){
//
//                Toast.makeText(this,"Password OK",Toast.LENGTH_LONG).show();
//            }else{
//
//                Snackbar.make(view, "Password is not the same, try again", Snackbar.LENGTH_LONG).show();
//                edRepPassword.setText(null);
//                edRepPassword.requestFocus();
//            }
//
//        }catch (Exception e){
//
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
//
//        }
//    }
    private boolean validateUsername(){
        int usernameLg;
        usernameLg = edUsername.getText().toString().trim().length();
         if(usernameLg==0){
             Toast.makeText(this,"Username is required",Toast.LENGTH_SHORT).show();
             edUsername.requestFocus();
             return false;
         }else{
             return true;
         }

    }
    private boolean validateFullname(){
        int fullnameLg;
        fullnameLg = edFullName.getText().toString().trim().length();
        if(fullnameLg==0){
            Toast.makeText(this,"The name is required",Toast.LENGTH_SHORT).show();
            edFullName.requestFocus();
            return false;
        }else{
            return true;
        }

    }

    private boolean validateEmail() {
        try {
            String email;
            Boolean matchRegex;

            email    = edEmail.getText().toString().trim();

            matchRegex  = Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email);

            if(matchRegex){

                Toast.makeText(this,"Email OK",Toast.LENGTH_SHORT).show();
                return true;
            }else{
                Toast.makeText(this,"Invalid Email",Toast.LENGTH_SHORT).show();
                edEmail.requestFocus();
                return false;
            }

        }catch (Exception e){

            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    private boolean validatePassword() {
        try {
            String password, repPassword;
            int matchPass;
            password    = edPassword.getText().toString().trim();
            repPassword = edRepPassword.getText().toString().trim();
            matchPass   = password.compareTo(repPassword);

            Boolean matchRegex;


            matchRegex  = Pattern.matches("^[A-Z](?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{4,10}$", password);

            if(!matchRegex){

                Toast.makeText(this,"Password does no met the requirements",Toast.LENGTH_SHORT).show();
                edPassword.requestFocus();
                return false;

            }else if(matchPass!=0){

                Toast.makeText(this,"Password does no match",Toast.LENGTH_SHORT).show();
               edRepPassword.setText(null);
               return  false;

            }
            else{
                Toast.makeText(this,"Password OK",Toast.LENGTH_SHORT).show();
                return true;

            }

        }catch (Exception e){

            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;

        }

    }



    private class ValidatorTextWatcher implements TextWatcher {
        private View view;

        public ValidatorTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch(view.getId()){
                case(R.id.edPassword):
                    validatePassword();
                    break;

                case(R.id.edEmail):
                    validateEmail();
                    break;
                case (R.id.edUsername):
                    validateUsername();
                    break;
                case(R.id.edFullName):
                    validateFullname();
                    break;
            }

        }
    }


}