package com.project.linkup;


import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDatabaseRef {
    public static  DatabaseReference userRef,postRef,chatRef;

    public FirebaseDatabaseRef(String userid){
        Log.e("FirebasedatabaseRef",USERID);
            userRef=FirebaseDatabase.getInstance().getReference("Users").child(userid);
            postRef=FirebaseDatabase.getInstance().getReference("Posts");
            chatRef=FirebaseDatabase.getInstance().getReference("Chats");
    }

    public void userStatus(String status) {
        userRef.child("status").setValue(status);
    }
//-------------------------------------------------------------------------------------------------------------->>
    public interface FirebaseCallback {
        void onCallback(String ProfileUrl,String UserName,String Name,String About,String Status);
    }

    public void userProfile(FirebaseCallback callback){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ProlifeUrl,UserName,Name,About,Status;

                    ProlifeUrl = snapshot.child("profileUrl").getValue(String.class);
                    UserName = snapshot.child("userName").getValue(String.class);
                    About = snapshot.child("about").getValue(String.class);
                    Name = snapshot.child("name").getValue(String.class);
                    Status=snapshot.child("status").getValue(String.class);


                    callback.onCallback(ProlifeUrl,UserName,Name,About,Status);
                } else {
                    Log.e("FirebaseRef Profile", "User not found in database.");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
//-------------------------------------------------------------------------------------------------------------->>>
    public interface PersonalInfo {
        void oncallbackPersonal(String Email,String PhoneNo,String Gender,String DOB);
    }
    public void userPersonal(PersonalInfo callbackPersonal){
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (currentUser!=null && snapshot.exists()) {
                    String Email,PhoneNo,Gender,DOB;
                    Email=snapshot.child("email").getValue(String.class);
                    PhoneNo=snapshot.child("phoneNo").getValue(String.class);
                    Gender=snapshot.child("gender").getValue(String.class);
                    DOB=snapshot.child("dob").getValue(String.class);
                    callbackPersonal.oncallbackPersonal(Email,PhoneNo,Gender,DOB);
                } else {
                    Log.e("FirebaseRef Profile", "User not found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
