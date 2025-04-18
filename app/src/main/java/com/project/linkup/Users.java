package com.project.linkup;

public class Users {
    String UserId,ProfileUrl,UserName,Name,PhoneNo, Gender, DOB, Status,About;
    int postCount;

    public Users(){ }

    public Users(String userId, String profileUrl, String userName, String name, String phoneNo, String gender, String DOB, String status, String about,int postCount) {
        this.UserId = userId;
        this.ProfileUrl = profileUrl;
        this.UserName = userName;
        this.Name = name;
        this.PhoneNo = phoneNo;
        this.Gender = gender;
        this.DOB = DOB;
        this.Status = status;
        this.About = about;
        this.postCount=postCount;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }
}
