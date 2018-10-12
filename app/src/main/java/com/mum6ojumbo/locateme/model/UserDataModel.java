package com.mum6ojumbo.locateme.model;

public class UserDataModel {
public String mEmail;
public String gUid;
public UserDataModel(String random,String email,String gUid){
    this.mEmail=email;
    this.gUid = gUid;
}
public UserDataModel(){}

    public void setgUid(String gUid) {
        this.gUid = gUid;
    }

    public void setmEmail(String mEmail){
        this.mEmail=mEmail;
    }

    public void setEmail(String email){}
    public String getEmail(){return this.mEmail;}
    public String getgUid(){return this.gUid;}
}
