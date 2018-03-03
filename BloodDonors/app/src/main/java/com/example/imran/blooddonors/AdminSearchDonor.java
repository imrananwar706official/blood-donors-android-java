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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminSearchDonor extends Activity {

    private Button searchbtn;
    private Spinner searchbldSpin,cityspin;
    private FirebaseAuth Auth;
    private FirebaseUser user;
    private String uid;
    private FirebaseAuth.AuthStateListener sAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_donor);

        initComponents();
        settingUpListeners();
    }

    private void settingUpListeners() {


        sAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if(Auth.getCurrentUser() != null){

                    if (user != null) {

                        uid = user.getUid();


                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){

                            Intent intentAuth = new Intent(AdminSearchDonor.this, AdminPanel.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();

                        }else {

                            Intent intentAuth = new Intent(AdminSearchDonor.this, SearchDonor.class);
                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intentAuth);
                            finish();
                        }
                    }


                }

            }};


        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {

                    String btype = searchbldSpin.getSelectedItem().toString().trim();
                    String city = cityspin.getSelectedItem().toString().trim();

                    Intent intent = new Intent(AdminSearchDonor.this,AdminSearchview.class);
                    intent.putExtra("bt", btype);
                    intent.putExtra("ct", city);
                    startActivity(intent);

                }catch (Exception e){

                    Toast.makeText(AdminSearchDonor.this,"Try Again!",Toast.LENGTH_SHORT).show();

                }
            }
        });




    }






    private void initComponents() {

        searchbtn = (Button) findViewById(R.id.btnsearchAdmin);
        searchbldSpin = (Spinner) findViewById(R.id.spinner_search_bldAdmin);
        cityspin = (Spinner) findViewById(R.id.spinner_search_cityAdmin);
        Auth = FirebaseAuth.getInstance();

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
        Intent i=new Intent(AdminSearchDonor.this, Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Auth.addAuthStateListener(sAuthListener);

   }

    @Override
    protected void onStop() {
        super.onStop();
        if(sAuthListener != null){
            Auth.removeAuthStateListener(sAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sAuthListener != null) {
            Auth.addAuthStateListener(sAuthListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sAuthListener != null) {
            Auth.removeAuthStateListener(sAuthListener);
        }
    }



    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AdminSearchDonor.this,AdminPanel.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
