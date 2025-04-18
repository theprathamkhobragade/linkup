package com.project.linkup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseActivity extends AppCompatActivity {
    private DatabaseReference userStatusRef;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            userStatusRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("status");

            // Set user status to online
            userStatusRef.setValue("Online");

            // Set offline when connection is lost
            userStatusRef.onDisconnect().setValue("Offline");
        }else{
            startActivity(new Intent(BaseActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userStatusRef != null) {
            userStatusRef.setValue("Online");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (userStatusRef != null) {
            userStatusRef.setValue("Away"); // Indicates app is running in the background
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userStatusRef != null) {
            userStatusRef.setValue("Offline");
        }
    }

}
