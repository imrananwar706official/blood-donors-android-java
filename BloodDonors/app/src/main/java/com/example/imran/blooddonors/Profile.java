package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Activity {

    private Button changePass;
    private TextView txtName,txtEmail,txtBlood,txtGender,txtCity;
    private ProgressBar pProgress;
    private FirebaseAuth profAuth;
    private FirebaseAuth.AuthStateListener  profauthListener;
    private FirebaseUser user;
    private DatabaseReference pRef;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponents();
        settingUpListeners();


    }

    private void settingUpListeners() {


//        profauthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if (profAuth.getCurrentUser() != null) {
//                    if (user != null) {
//
//                        uid = user.getUid();
//
//
//                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){
//
//                            Intent intentAuth = new Intent(Profile.this, AdminPanel.class);
//                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intentAuth);
//                            finish();
//
//                        }else {
//
//                            Intent intentAuth = new Intent(Profile.this, Profile.class);
//                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intentAuth);
//                            finish();
//                        }
//                    }
//                }
//            }
//
//        };

        pProgress.setVisibility(View.VISIBLE);



        user = profAuth.getCurrentUser();

        if(user != null) {
            uid = user.getUid();

        }


        pRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child(uid).child("name").getValue(String.class);
                String blood = dataSnapshot.child(uid).child("blood_Group").getValue(String.class);
                String city = dataSnapshot.child(uid).child("city").getValue(String.class);
                String gender = dataSnapshot.child(uid).child("gender").getValue(String.class);
                String email = dataSnapshot.child(uid).child("email").getValue(String.class);

                txtName.setText(name);
                txtEmail.setText(email);
                txtBlood.setText(blood);
                txtGender.setText(gender);
                txtCity.setText(city);

                pProgress.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent passintent = new Intent(Profile.this,ChangePass.class);
                passintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(passintent);



            }
        });


    }






    private void initComponents() {
        txtName =(TextView) findViewById(R.id.txtProfnameset);
        txtBlood =(TextView) findViewById(R.id.txtProfBldIdset);
        txtGender =(TextView) findViewById(R.id.txtProfGenderset);
        txtCity =(TextView) findViewById(R.id.txtProfCityset);
        txtEmail =(TextView) findViewById(R.id.txtProfEmailset);
        changePass =(Button) findViewById(R.id.changePass);
        pProgress = (ProgressBar) findViewById(R.id.profProg);
        profAuth = FirebaseAuth.getInstance();
        pRef = FirebaseDatabase.getInstance().getReference("users");


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout1:

                logOut();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();
        Intent i=new Intent(Profile.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }


    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Profile.this,SearchDonor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        profAuth.addAuthStateListener(profauthListener);
//
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(profauthListener != null){
//            profAuth.removeAuthStateListener(profauthListener);
//        }
//    }


}
