package com.example.ashclean;

public class User {
    String userName, email;
    boolean isAnAdmin;

    double userCredit;

    public User(){

    }

    public User(String userName, String email, boolean isAnAdmin){
        this.userName = userName;
        this.email = email;
        this.isAnAdmin = isAnAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public double getUserCredit() {
        return userCredit;
    }

    public boolean getAnAdmin(){
        return  this.isAnAdmin;
    }

    public void setAnAdmin(){
        this.isAnAdmin = true;
    }

    public void setUserCredit(double credit){
        this.userCredit = credit;
    }






    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
