package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePass extends Activity {

    private EditText oldPass,newPass;
    private Button changePassw;
    private FirebaseAuth.AuthStateListener pAuthListener;
    private FirebaseAuth fAuth;
    private ProgressBar mprogress;
    private FirebaseUser user;
    private String uid;
    private AuthCredential credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        initComponents();
        settingUpListeners();



    }

    private void settingUpListeners() {


        pAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (fAuth.getCurrentUser() != null) {
                    if (user != null) {

                        uid = user.getUid();


                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){

                            Intent intentAuth = new Intent(ChangePass.this, AdminPanel.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();

                        }else {

                            Intent intentAuth = new Intent(ChangePass.this, SearchDonor.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();
                        }
                    }
                }

            }
        };



        changePassw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String oldPassword = oldPass.getText().toString().trim();
                final String newPassword = newPass.getText().toString().trim();

                user = FirebaseAuth.getInstance().getCurrentUser();

                    final String email = user.getEmail();

                credential = EmailAuthProvider.getCredential(email,oldPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){


                            mprogress.setVisibility(View.VISIBLE);
                if (user != null && !newPassword.equals("")) {
                    if (newPassword.length() < 6) {
                        newPass.setError("Password too short, enter minimum 6 characters");
                        mprogress.setVisibility(View.GONE);
                    } else {
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChangePass.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                            fAuth.signOut();
                                            mprogress.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(ChangePass.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                            mprogress.setVisibility(View.GONE);
                                        }
                                    }
                                });
                    }
                } else if (newPassword.equals("")) {
                    newPass.setError("Enter password");
                    mprogress.setVisibility(View.GONE);
                }




            }
        }
                }

        );


            }

        }

        );






    }

    private void initComponents() {
        oldPass = (EditText) findViewById(R.id.pass_changeOld);
        newPass = (EditText) findViewById(R.id.pass_changeNew);
        changePassw = (Button) findViewById(R.id.btnChangPass);
        fAuth = FirebaseAuth.getInstance();
        mprogress =(ProgressBar) findViewById(R.id.changPasprog);


    }

    @Override
    protected void onStart(){
        super.onStart();

        fAuth.addAuthStateListener(pAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(pAuthListener != null){
            fAuth.removeAuthStateListener(pAuthListener);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (pAuthListener != null) {
            fAuth.addAuthStateListener(pAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pAuthListener != null) {
            fAuth.removeAuthStateListener(pAuthListener);
        }
    }


}
