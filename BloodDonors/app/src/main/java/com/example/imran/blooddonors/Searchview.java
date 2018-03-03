package com.example.imran.blooddonors;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imran.blooddonors.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Searchview extends Activity {

    private ListView listResult;
    private ArrayAdapter<User> adapter;
    private FirebaseAuth.AuthStateListener sAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser userd;
    private String uid;
    private DatabaseReference mRef;
    private ArrayList<User> data;
    private ProgressBar searchProgress;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchview);

        initComponents();
        settingUpListeners();
        checkRuntimePermissions();

       }



    private void settingUpListeners() {

        //Setting Up Listeners

        sAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (mAuth.getCurrentUser() != null) {
                    if (userd != null) {

                        uid = userd.getUid();


                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){

                            Intent intentAuth = new Intent(Searchview.this, AdminPanel.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();

                        }else {

                            Intent intentAuth = new Intent(Searchview.this, SearchDonor.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();
                        }
                    }
                }

            }
        };


        final String bloodgrp = getIntent().getStringExtra("bt");
        final String city=getIntent().getStringExtra("ct");

        searchProgress.setVisibility(View.VISIBLE);

        final Query query = mRef.orderByChild("blood_Group").equalTo(bloodgrp);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                if(dataSnapshot.getChildrenCount() > 0){

                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot child : children) {

                    User value = child.getValue(User.class);

                    if(value.getCity().equals(city) && value.isDonor()) {
                        data.clear();

                        data.add(value);

                        adapter.notifyDataSetChanged();

                    }

                    searchProgress.setVisibility(View.GONE);
                    if (data.size() == 0){
                        Toast.makeText(Searchview.this, "Donors not Found!",Toast.LENGTH_SHORT).show();
                    }



                    }


                }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //List Listener


        listResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {


               User user = adapter.getItem(position);

                String name = user.getName();
               final String phone = user.getMobile();



                final Dialog dialog = new Dialog(Searchview.this);


                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_user_details);
                dialog.setTitle("Call Donor");

                //adding text dynamically

                TextView t1 = (TextView) dialog.findViewById(R.id.txtname);
                TextView t2 = (TextView) dialog.findViewById(R.id.txtBldtype);
                TextView t3 = (TextView) dialog.findViewById(R.id.txtphone);
                TextView t4 = (TextView) dialog.findViewById(R.id.txtcity);

                t1.setText(name);
                t2.setText(bloodgrp);
                t3.setText(phone);
                t4.setText(city);
                //adding button click event
                Button call = (Button) dialog.findViewById(R.id.callbutt);
                Button cancel = (Button) dialog.findViewById(R.id.cancelbutt);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //user granted all permissions we can perform our task.
                        if(ActivityCompat.checkSelfPermission(Searchview.this,
                                android.Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){

                            Toast.makeText(Searchview.this,"Call permission required!",Toast.LENGTH_SHORT).show();




                    }else {
                            //user granted all permissions we can perform our task.

                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + phone));
                            startActivity(callIntent);
                        }




                    }}


                );


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });



            }


    private void initComponents() {

        //init Components

        data = new ArrayList<User>();
        listResult = (ListView) findViewById(R.id.listview12);
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1,data);
        //adapter.addAll();
        listResult.setAdapter(adapter);
       mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference("users");
        searchProgress = (ProgressBar) findViewById(R.id.progressBarSearch);
    }


    private void checkRuntimePermissions() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.CALL_PHONE )!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(Permissions.PERMISSIONS,Permissions.PERMISSION_CODE_CALL_PHONE);
            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case Permissions.PERMISSION_CODE_CALL_PHONE:{
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){





                }else {

                    Toast.makeText(Searchview.this,"Call Permission required!",Toast.LENGTH_SHORT).show();

                }
            }

            break;
        }
    }











    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuprof, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.profile:
                Intent profintent = new Intent(Searchview.this,Profile.class);
                profintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(profintent);
                finish();

                return true;
            case R.id.logout:

                logOut();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {

        FirebaseAuth.getInstance().signOut();
        Intent i=new Intent(Searchview.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(sAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(sAuthListener != null){
            mAuth.removeAuthStateListener(sAuthListener);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sAuthListener != null) {
            mAuth.addAuthStateListener(sAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sAuthListener != null) {
            mAuth.removeAuthStateListener(sAuthListener);
        }
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Searchview.this,SearchDonor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         startActivity(intent);
    }




}
