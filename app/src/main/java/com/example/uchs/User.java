package com.example.uchs;

public class User {
    public String[] userName;
    public String userPh;
    public String userID;
    public String userPass;
    public String userProf;
    public String userAge;

    User(String name, String phone, String uid, String uPass, String uProf, String uAge) {
        userName = name.split(" ",2);
        userPh = phone;
        userID = uid;
        userPass = uPass;
        userProf = uProf;
        userAge = uAge;
    }
    public String genUserString() {
        String msg = "\n";
        int i = 1;
        for (String nameElms : userName) {
            msg += String.valueOf(i) + " " + nameElms + "\n";
            i ++;
        }
        msg += "UserPhone: " + userPh + "\n";
        msg += "UserID: " + userID + "\n";
        msg += "UserPass: " + userPass + "\n";
        msg += "UserProf: " + userProf + "\n";
        msg += "UserAge: " + userAge + "\n";
        return msg;
    }
}
