package com.example.imran.blooddonors.model;

/**
 * Created by IMRAN on 1/20/2018.
 */

public class RegisterModel {

    private String Name;
    private String Mobile;
    private String Email;
    private String City;
    private String Password;
    private String Blood_Group;
    private String Gender;
    private String uid;
    private boolean Donor;

    public RegisterModel(String name, String mobile, String email, String city, String password, String blood_Group, String gender, String uid, boolean donor) {
        Name = name;
        Mobile = mobile;
        Email = email;
        City = city;
        Password = password;
        Blood_Group = blood_Group;
        Gender = gender;
        this.uid = uid;
        Donor = donor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }





    public boolean isDonor() {
        return Donor;
    }

    public void setDonor(boolean donor) {
        Donor = donor;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBlood_Group() {
        return Blood_Group;
    }

    public void setBlood_Group(String blood_Group) {
        Blood_Group = blood_Group;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }


    public RegisterModel() {

    }
}
