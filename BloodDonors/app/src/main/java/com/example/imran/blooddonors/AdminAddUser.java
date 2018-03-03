package com.example.imran.blooddonors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imran.blooddonors.model.RegisterModel;
import com.example.imran.blooddonors.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminAddUser extends Activity {

    private DatabaseReference addDatabase;
    private FirebaseAuth authUser;
    private FirebaseUser user;
//    private FirebaseAuth.AuthStateListener addUserListener;
    private Button addUser;
    private TextView txtreg,txtbldgrp,txtgen;
    private EditText name,contact,email,passwd;
    private Spinner blgrp,city;
    private ProgressBar mprogress;
    private RadioGroup radioGroupgen;
    private RadioButton gender;
    private String uid;
    private CheckBox donorCheck;
    private String strDonor= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        initComponents();
        settingUpListeners();
    }

    private void settingUpListeners() {


//        addUserListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                if(authUser.getCurrentUser() != null){
//
//                    if (user != null) {
//
//                        uid = user.getUid();
//
//
//                        if(uid.equals("TRqlMF1aAkgJCL8VBEIJbGEJD1j2")){
//
//                            Intent intentAuth = new Intent(AdminAddUser.this, AdminPanel.class);
//                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intentAuth);
//                            finish();
//
//                        }else {
//
//                            Intent intentAuth = new Intent(AdminAddUser.this, SearchDonor.class);
//                            intentAuth.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intentAuth);
//                            finish();
//                        }
//                    }
//                }
//            }
//
//        };



        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{


                    final String namel = name.getText().toString().trim();
                    final String mobile1 = contact.getText().toString().trim();
                    final String cityl = city.getSelectedItem().toString().trim();
                    final String bloodl = blgrp.getSelectedItem().toString().trim();
                    final String emaill = email.getText().toString().trim();
                    final String passwdl = passwd.getText().toString().trim();
                    int genid = radioGroupgen.getCheckedRadioButtonId();
                    gender = (RadioButton) findViewById(genid);
                    final String genderl= gender.getText().toString();


                    boolean isDonor = donorCheck.isChecked();

                    if(donorCheck.isChecked())
                    {
                        strDonor = "Yes";
                    }
                    else
                    {
                        strDonor = "No";
                    }

//                    boolean isAdmin = adminCheck.isChecked();
//
//                    if(adminCheck.isChecked())
//                    {
//                        strAdmin = "Yes";
//                    }
//                    else
//                    {
//                        strAdmin = "No";
//                    }

                    final User addNewUser = new User();

                    addNewUser.setName(namel);
                    addNewUser.setMobile(mobile1);
                    addNewUser.setBlood_Group(bloodl);
                    addNewUser.setGender(genderl);
                    addNewUser.setEmail(emaill);
                    addNewUser.setPassword(passwdl);
                    addNewUser.setCity(cityl);
                    addNewUser.setDonor(isDonor);
//                    addNewUser.setAdmin(isAdmin);


                    if (TextUtils.isEmpty(addNewUser.getName()) && TextUtils.isEmpty(addNewUser.getEmail())&& TextUtils.isEmpty(addNewUser.getPassword()) && TextUtils.isEmpty(addNewUser.getMobile())  ) {
                        Toast.makeText(AdminAddUser.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();


                    } else {

                        mprogress.setVisibility(View.VISIBLE);

                        if (addNewUser.getMobile().isEmpty()){

                            contact.setError("Enter Phone number!");
                            mprogress.setVisibility(View.GONE);

                        }


                        if (!isValidEmailAddress(addNewUser.getEmail())){

                            email.setError("Enter valid Email!");
                            mprogress.setVisibility(View.GONE);
                        }

                        if (!isValidName(addNewUser.getName())){

                            name.setError("Enter valid Name!");
                            mprogress.setVisibility(View.GONE);

                        }

                        if(!isValidPhone(addNewUser.getMobile())){

                            contact.setError("Enter valid Phone number!");
                            mprogress.setVisibility(View.GONE);

                        }

                        if (addNewUser.getPassword().length() < 6) {
                            passwd.setError(getString(R.string.minimum_password));
                            mprogress.setVisibility(View.GONE);
                            Toast.makeText(AdminAddUser.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();


                        }


                        authUser.fetchProvidersForEmail(addNewUser.getEmail()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                                boolean checkEmail = !task.getResult().getProviders().isEmpty();

                                if(!checkEmail){

                                    authUser.createUserWithEmailAndPassword(addNewUser.getEmail(), addNewUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {


                                            if (task.isSuccessful()) {

                                                String user = authUser.getCurrentUser().getUid();

                                                DatabaseReference current_user_db = addDatabase.child(user);

                                                addNewUser.setUid(user);




                                                // HashMap<String, String> editData = new HashMap<String, String>();
                                                // editData.put("Name", register.getName());
                                                // editData.put("Mobile", register.getMobile());
                                                // editData.put("Email", register.getEmail());
                                                // editData.put("City", register.getCity());
                                                // editData.put("Password", register.getPassword());
                                                // editData.put("Blood_Group", register.getBlood_Group());
                                                // editData.put("Gender", register.getGender());

                                                current_user_db.setValue(addNewUser);




                                                Toast.makeText(AdminAddUser.this, "Successfully Added!", Toast.LENGTH_SHORT).show();

                                                mprogress.setVisibility(View.GONE);


                                            }else {

                                                mprogress.setVisibility(View.GONE);


                                                Toast.makeText(AdminAddUser.this, "Failed!", Toast.LENGTH_SHORT).show();

                                            }


                                        }
                                    });

                                }else{

                                    Toast.makeText(AdminAddUser.this,"Email is already registered!",Toast.LENGTH_SHORT).show();
                                    mprogress.setVisibility(View.GONE);

                                }

                            }
                        });

//                        authUser.createUserWithEmailAndPassword(registerUser.getEmail(), registerUser.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                                if (task.isSuccessful()) {
//
//                                    String user = authUser.getCurrentUser().getUid();
//
//                                    DatabaseReference current_user_db = addDatabase.child(user);
//
//                                    registerUser.setUid(user);
//
//
//
//
//                                    // HashMap<String, String> editData = new HashMap<String, String>();
//                                    // editData.put("Name", register.getName());
//                                    // editData.put("Mobile", register.getMobile());
//                                    // editData.put("Email", register.getEmail());
//                                    // editData.put("City", register.getCity());
//                                    // editData.put("Password", register.getPassword());
//                                    // editData.put("Blood_Group", register.getBlood_Group());
//                                    // editData.put("Gender", register.getGender());
//
//                                    current_user_db.setValue(registerUser);
//
//
//
//
//                                    Toast.makeText(AdminAddUser.this, "Successfully Added!", Toast.LENGTH_SHORT).show();
//
//                                    mprogress.setVisibility(View.GONE);
//
//
//                                }else {
//
//                                    mprogress.setVisibility(View.GONE);
//
//
//                                    Toast.makeText(AdminAddUser.this, "Failed!", Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                            }
//                        });


                    }}catch (Exception e){


                    Toast.makeText(AdminAddUser.this,"Please fill all the details.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Email validation code

    public boolean isValidEmailAddress(String email) {


        String ePattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern ep = java.util.regex.Pattern.compile(ePattern);
        Matcher em = ep.matcher(email);
        if (!em.find()) {

            if (TextUtils.isEmpty(this.email.getText().toString())) {
                Toast.makeText(AdminAddUser.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
            }
            return false;

        }
        else{

            return true;

        }
    }

    // Name Validation

    public boolean isValidName(String name) {
        String nPattern = "[A-Za-z][a-zA-Z]+[a-zA-z]+([ ][a-zA-Z]+)*";

        Pattern np = java.util.regex.Pattern.compile(nPattern);
        Matcher nm = np.matcher(name);
        if (!nm.find()) {

            if (TextUtils.isEmpty(this.name.getText().toString())) {
                Toast.makeText(AdminAddUser.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
            }
            return false;

        }
        else{

            return true;

        }
    }

    // Phone validation

    public boolean isValidPhone(String phone) {

        String ePattern = "^\\(?(\\d{11})\\)?$";

        Pattern pp = java.util.regex.Pattern.compile(ePattern);
        Matcher pm = pp.matcher(phone);
        if (!pm.find()) {

            if (TextUtils.isEmpty(this.contact.getText().toString())) {
                Toast.makeText(AdminAddUser.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
            }

            return false;

        }
        else{

            return true;

        }
    }


    private void initComponents() {




        // get reference to 'users' node
        addDatabase =  FirebaseDatabase.getInstance().getReference().child("users");
        name = (EditText) findViewById(R.id.editNameAdmin);
        contact = (EditText) findViewById(R.id.editContAdmin);
        addUser = (Button) findViewById(R.id.btnAddDonor);
        blgrp = (Spinner) findViewById(R.id.spinnerBlgAdmin);
        city = (Spinner) findViewById(R.id.spinnerCityAdmin);
        passwd = (EditText) findViewById(R.id.editPassAdmin);
        email = (EditText) findViewById(R.id.editEmailAdmin);
        radioGroupgen = (RadioGroup) findViewById(R.id.radioGenAdmin);
        txtreg = (TextView) findViewById(R.id.txtRegAdmin);
        txtgen = (TextView) findViewById(R.id.txtGenAdmin);
        txtbldgrp = (TextView) findViewById(R.id.txtBldgrpAdmin);
        authUser = FirebaseAuth.getInstance();
        mprogress = (ProgressBar) findViewById(R.id.progressBarregAdmin);
        donorCheck = (CheckBox) findViewById(R.id.donoruser_check);
//        adminCheck = (CheckBox) findViewById(R.id.admin_check);


    }


//    @Override
//    protected void onStart(){
//        super.onStart();
//
//        authUser.addAuthStateListener(addUserListener);
//
//    }
//
//    @Override
//    protected void onStop(){
//        super.onStop();
//        if(addUserListener != null){
//            authUser.removeAuthStateListener(addUserListener);
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (addUserListener != null) {
//            authUser.addAuthStateListener(addUserListener);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (addUserListener != null) {
//            authUser.removeAuthStateListener(addUserListener);
//        }
//    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(AdminAddUser.this,AdminPanel.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}
