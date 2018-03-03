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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends Activity {


    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth auth;
    private Button register;
    private TextView txtreg,txtbldgrp,txtgen;
    private EditText name,email,contact,passwd;
    private Spinner blgrp,city;
    private ProgressBar mprogress;
    private RadioGroup radioGroupgen;
    private RadioButton gender;
    private CheckBox donCheck;
    private String strDonor="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


           initComponents();
           settingUpListeners();


              }

    private void settingUpListeners() {



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{


             final String namel = name.getText().toString().trim();
             final String mobile1 = contact.getText().toString().trim();
             final String emaill = email.getText().toString().trim();
             final String cityl = city.getSelectedItem().toString().trim();
             final String paswdl = passwd.getText().toString().trim();
             final String bloodl = blgrp.getSelectedItem().toString().trim();
             int genid = radioGroupgen.getCheckedRadioButtonId();
             gender = (RadioButton) findViewById(genid);
             final String genderl= gender.getText().toString();


                    boolean check = donCheck.isChecked();

                    if(donCheck.isChecked())
                            {
                                strDonor = "Yes";
                            }
                            else
                            {
                                strDonor = "No";
                            }

                final RegisterModel register = new RegisterModel();

                register.setName(namel);
                register.setMobile(mobile1);
                register.setBlood_Group(bloodl);
                register.setGender(genderl);
                register.setCity(cityl);
                register.setEmail(emaill);
                register.setPassword(paswdl);
                register.setDonor(check);


                if (TextUtils.isEmpty(register.getName()) && TextUtils.isEmpty(register.getMobile()) && TextUtils.isEmpty(register.getEmail()) && TextUtils.isEmpty(register.getPassword())) {
                    Toast.makeText(Register.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();



                } else {

                    mprogress.setVisibility(View.VISIBLE);

                    if (register.getMobile().isEmpty()){

                        contact.setError("Enter Phone number!");
                        mprogress.setVisibility(View.GONE);

                    }


                    if (!isValidEmailAddress(register.getEmail())){

                        email.setError("Enter valid Email!");
                        mprogress.setVisibility(View.GONE);
                    }

                    if (!isValidName(register.getName())){

                        name.setError("Enter valid Name!");
                        mprogress.setVisibility(View.GONE);

                    }

                    if(!isValidPhone(register.getMobile())){

                        contact.setError("Enter valid Phone number!");
                        mprogress.setVisibility(View.GONE);

                    }

                    if (register.getPassword().length() < 6) {
                        passwd.setError(getString(R.string.minimum_password));
                        mprogress.setVisibility(View.GONE);
                        Toast.makeText(Register.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();


                    }

                    auth.fetchProvidersForEmail(register.getEmail()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                            boolean checkEmail = !task.getResult().getProviders().isEmpty();

                            if(!checkEmail){

                                auth.createUserWithEmailAndPassword(register.getEmail(), register.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                String user = auth.getCurrentUser().getUid();

                                DatabaseReference current_user_db = mFirebaseDatabase.child(user);

                                register.setUid(user);




                               // HashMap<String, String> editData = new HashMap<String, String>();
                               // editData.put("Name", register.getName());
                               // editData.put("Mobile", register.getMobile());
                               // editData.put("Email", register.getEmail());
                               // editData.put("City", register.getCity());
                               // editData.put("Password", register.getPassword());
                               // editData.put("Blood_Group", register.getBlood_Group());
                               // editData.put("Gender", register.getGender());

                                current_user_db.setValue(register);




                                Toast.makeText(Register.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(Register.this, SearchDonor.class);
                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(home);
                                finish();


                            }else {

                                mprogress.setVisibility(View.GONE);


                                Toast.makeText(Register.this, "Enter Valid Details!!", Toast.LENGTH_SHORT).show();

                            }    }
                                });  }else{

                                Toast.makeText(Register.this,"Email is already registered!",Toast.LENGTH_SHORT).show();
                                mprogress.setVisibility(View.GONE);

                            }


                        }
                                });





                }}catch (Exception e){


                    Toast.makeText(Register.this,"Please fill all the details.",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Register.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Register.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Register.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
            }

            return false;

        }
        else{

            return true;

        }
    }


    private void initComponents() {




        // get reference to 'users' node
        mFirebaseDatabase =  FirebaseDatabase.getInstance().getReference().child("users");
        name = (EditText) findViewById(R.id.editName);
        contact = (EditText) findViewById(R.id.editCont);
        register = (Button) findViewById(R.id.btnReg2);
        blgrp = (Spinner) findViewById(R.id.spinnerBlg);
        city = (Spinner) findViewById(R.id.spinnerCity);
        passwd = (EditText) findViewById(R.id.editPassreg);
        email = (EditText) findViewById(R.id.editEmail);
        radioGroupgen = (RadioGroup) findViewById(R.id.radioGen);
        txtreg = (TextView) findViewById(R.id.txtReg);
        txtgen = (TextView) findViewById(R.id.txtGen);
        txtbldgrp = (TextView) findViewById(R.id.txtBldgrp);
        auth = FirebaseAuth.getInstance();
        mprogress = (ProgressBar) findViewById(R.id.progressBarreg);
        donCheck = (CheckBox) findViewById(R.id.donor_check);

    }



    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Register.this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}



