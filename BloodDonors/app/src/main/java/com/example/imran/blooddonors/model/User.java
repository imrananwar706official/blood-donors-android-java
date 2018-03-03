package com.example.imran.blooddonors.model;

/**
 * Created by IMRAN on 1/22/2018.
 */



   public class User {

        private String Name;
        private String Mobile;
        private String Email;
        private String City;
        private String Blood_Group;
        private String Gender;
        private String Password;
        private String uid;
        private boolean Donor;
//        private boolean Admin;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

//    public boolean isAdmin() {
//        return Admin;
//    }
//
//    public void setAdmin(boolean admin) {
//        Admin = admin;
//    }



    public User(String name, String mobile, String email, String city, String blood_Group, String gender, String password, String uid, boolean donor) {
        Name = name;
        Mobile = mobile;
        Email = email;
        City = city;
        Blood_Group = blood_Group;
        Gender = gender;
        Password = password;
        this.uid = uid;
        Donor = donor;
//        Admin = admin;
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



    @Override
    public String toString() {
        return Name;
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





    public User() {

    }
}



