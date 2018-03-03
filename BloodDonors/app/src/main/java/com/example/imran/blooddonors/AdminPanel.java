package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminPanel extends Activity {

    private Button btnSearch,btnAddUser,btnLogout;
    private FirebaseAuth adminPanelAuth;
    private FirebaseUser user;
    private String uid;
    private FirebaseAuth.AuthStateListener adminListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);


        initComponents();
        settingUpListeners();


    }

    private void settingUpListeners() {


        adminListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(adminPanelAuth.getCurrentUser() != null){

                    if (user != null) {

                        uid = user.getUid();


                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){

                            Intent intentAuth = new Intent(AdminPanel.this, AdminPanel.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();

                        }else {

                            Intent intentAuth = new Intent(AdminPanel.this, SearchDonor.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();
                        }
                    }

                }


            }

        };

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentSearch = new Intent(AdminPanel.this, AdminSearchDonor.class);
                intentSearch.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intentSearch);

            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAddUser = new Intent(AdminPanel.this, AdminAddUser.class);
                intentAddUser.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentAddUser);


            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logOut();



            }
        });

    }

    private void initComponents() {


        btnSearch = (Button) findViewById(R.id.btnAdminPanelSearch);
        btnAddUser = (Button) findViewById(R.id.btnAdminPanelAdd);
        btnLogout = (Button) findViewById(R.id.btnAdminPanelLogout);
        adminPanelAuth = FirebaseAuth.getInstance();

    }


    private void logOut() {

        FirebaseAuth.getInstance().signOut();
        Intent i=new Intent(AdminPanel.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    protected void onStart(){
        super.onStart();

        adminPanelAuth.addAuthStateListener(adminListener);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (adminListener != null) {
            adminPanelAuth.addAuthStateListener(adminListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adminListener != null) {
            adminPanelAuth.removeAuthStateListener(adminListener);
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        if (adminListener != null) {
            adminPanelAuth.removeAuthStateListener(adminListener);

        }
    }



}
