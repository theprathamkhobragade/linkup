package com.project.linkup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static Dialog loadingDialog;
    public static String USERID;
    public static FirebaseAuth firebaseAuth;
    public static FirebaseUser currentUser;
    public static FirebaseDatabaseRef firebaseDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
        firebaseAuth=FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    USERID=currentUser.getUid();
                    firebaseDatabaseRef=new FirebaseDatabaseRef(USERID);

                    startActivity(new Intent(MainActivity.this, Activity4_home.class));
                    finish();

                } else {
                    // No user is signed in, go to login screen
                    Toast.makeText(MainActivity.this, "User not signed in ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Activity1_login.class));
                    finish();
                }
            }
        },4000);

    }

}