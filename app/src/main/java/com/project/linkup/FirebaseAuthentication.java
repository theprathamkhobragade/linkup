package com.project.linkup;



import static com.project.linkup.MainActivity.firebaseAuth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication {
    FirebaseUser USER;

    public FirebaseAuthentication(){
        USER = FirebaseAuth.getInstance().getCurrentUser();
    }


    //--------------------------------------------------------------------------------------------------------------
    public interface ChangedEmailCallback {
        void onCallbackEmail(String email);
    }

    public void getEmail(ChangedEmailCallback callbackEmail) {
        if (USER != null) {
            USER.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String updatedEmail = USER.getEmail();
                    if (updatedEmail != null) {
                        callbackEmail.onCallbackEmail(updatedEmail);
                    } else {
                        callbackEmail.onCallbackEmail("Email not found");
                    }
                } else {
                    callbackEmail.onCallbackEmail("Failed to reload user: " + task.getException().getMessage());
                }
            });
        } else {
            callbackEmail.onCallbackEmail("User not found");
        }
    }
//--------------------------------------------------------------------------------------------------------------

}
