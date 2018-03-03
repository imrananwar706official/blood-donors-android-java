package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.imran.blooddonors.model.LoginModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends Activity {

    private Button Regist,Loginb,btnReset;
    private EditText email,passwd;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authListener;
    private ProgressBar progressBar;
    private String uid;
    private boolean flag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        initComponents();
        settingUpListeners();

    }

    private void settingUpListeners() {

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser() != null && flag){

                    if (user != null) {

                        uid = user.getUid();


                    if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){

                        Intent intentAuth = new Intent(Login.this, AdminPanel.class);
                        intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentAuth);
                        finish();

                    }else {

                        Intent intentAuth = new Intent(Login.this, SearchDonor.class);
                        intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentAuth);
                        finish();
                    }
                }
                    flag = false;
                }
            }

        };


        //Login button
        Loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String Email = email.getText().toString().trim();
                final String password = passwd.getText().toString().trim();

                LoginModel login = new LoginModel();
                login.setEmail(Email);
                login.setPassword(password);

                if (TextUtils.isEmpty(login.getEmail())){
                    Toast.makeText(Login.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(login.getPassword())) {
                    Toast.makeText(Login.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);



                if(login.getEmail().equals("admin@admin.com")){

                    auth.signInWithEmailAndPassword(login.getEmail(),login.getPassword()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            progressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.

                            if (!task.isSuccessful()) {
                                // If Authentication failed.
                                Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }else {

                                Intent intent = new Intent(Login.this, AdminPanel.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else {

                    //authenticate user
                    auth.signInWithEmailAndPassword(login.getEmail(),login.getPassword()).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            progressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.

                            if (!task.isSuccessful()) {
                                // If Authentication failed.
                                Toast.makeText(Login.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }else {

                                Intent intent = new Intent(Login.this, SearchDonor.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                }
                }
        });


        //Reset button

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentReset = new Intent(Login.this, ResetPass.class);
                intentReset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentReset);
            }
        });





                // Sign up button

        Regist.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {

                                          Intent intent = new Intent(Login.this, Register.class);
                                          startActivity(intent);
                                          finish();

                                      }
                                  });

    }




    private void initComponents() {

        btnReset = (Button) findViewById(R.id.btn_reset_password);
        Regist = (Button)findViewById(R.id.btnReg);
        Loginb = (Button)findViewById(R.id.btnLog);
        email = (EditText)findViewById(R.id.editEmailog);
        passwd = (EditText)findViewById(R.id.editPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

    }


    @Override
    protected void onStart(){
        super.onStart();

        auth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authListener != null){
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authListener != null) {
            auth.addAuthStateListener(authListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }



}



