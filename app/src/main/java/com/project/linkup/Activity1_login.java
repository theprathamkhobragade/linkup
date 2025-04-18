package com.project.linkup;

import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseAuth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Activity1_login extends AppCompatActivity {
    TextView Email,Phone;

    LinearLayout layout1,layout2;
    TextView text1,text2,textForgetPassword,textSignin;
    EditText edit1,edit2;
    Button buttonSubmit;
    Boolean clickedEmail,clickedPhone;

    Dialog loadingDialog;

    String storedVerificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;

    AlertDialog.Builder alertDailog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity1_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Email=findViewById(R.id.Email);
        Phone=findViewById(R.id.Phone);

        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        textForgetPassword=findViewById(R.id.textForgetPassword);

        buttonSubmit=findViewById(R.id.buttonSubmit);
        textSignin=findViewById(R.id.textSignin);

        alertDailog = new AlertDialog.Builder(Activity1_login.this);

        textSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity1_login.this,Activity2_signin.class));
            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedEmail=true; clickedPhone=false;
                Email.setBackgroundResource(R.drawable.border1); Phone.setBackground(null);

                text1.setText("Email");

                edit1.setText(null);
                edit1.setHint("abc@gmail.com");
                edit1.setFocusable(true);
                edit1.setFocusableInTouchMode(true);
                edit1.requestFocus();
                edit1.setInputType(InputType.TYPE_CLASS_TEXT);
                edit1.setFilters(new InputFilter.LengthFilter[]{});

                layout2.setVisibility(View.VISIBLE);
                text2.setText("Password");
                edit2.setText(null);
                textForgetPassword.setVisibility(View.VISIBLE);
                buttonSubmit.setText("Login");
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPhone=true; clickedEmail=false;
                Phone.setBackgroundResource(R.drawable.border1); Email.setBackground(null);

                text1.setText("Phone No");
                edit1.setText(null);
                edit1.setHint("0000000000");
                edit1.setFocusable(true);
                edit1.setFocusableInTouchMode(true);
                edit1.requestFocus();
                edit1.setInputType(InputType.TYPE_CLASS_PHONE);
                edit1.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(10)});

                layout2.setVisibility(View.GONE);
                edit2.setText(null);
                textForgetPassword.setVisibility(View.GONE);
                buttonSubmit.setText("Send OTP");
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_foregetpassword, null);
        EditText forgertEmail = dialogView.findViewById(R.id.forgertEmail);

        AlertDialog.Builder builder = new AlertDialog.Builder(Activity1_login.this);
        textForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=edit1.getText().toString();
                forgertEmail.setText(email);

                // Remove from parent if already attached
                ViewGroup parent = (ViewGroup) dialogView.getParent();
                if (parent != null) {
                    parent.removeView(dialogView);
                }
                builder.setView(dialogView);
                builder.setTitle("Foreget Password!");
                builder.setCancelable(false);
                builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = forgertEmail.getText().toString();
//                        sendLink(email);
                        edit1.setText(null);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickedPhone){
                    String PhoneNo=edit1.getText().toString();

                    if(!(buttonSubmit.getText().toString().equals("verify"))){
                        if (PhoneNo.length() == 10) {
                            sendVerificationCode("+91" + PhoneNo);
                            edit1.setFocusable(false);
                            layout2.setVisibility(View.VISIBLE);
                            text2.setText("OTP");
                            buttonSubmit.setText("verify");
                        }
                        else {
                            edit1.setError("Enter valid phone number");
                        }
                    }
                    else {
                        String code = edit2.getText().toString().trim();
                        if (!code.isEmpty() && storedVerificationId != null) {
                            loadingDialog.show();
                            verifyCode(code);
                        }
                    }
                }
                else {
                    String Email=edit1.getText().toString();
                    String Password=edit2.getText().toString();

                    if(Email.isEmpty()|| Password.isEmpty()){
                        if(Email.isEmpty()){
                            edit1.setError("Enter Email");
                        }
                        if(Password.isEmpty()){
                            edit2.setError("Enater Password");
                        }
                    }else {
                        EmailLogin(Email,Password);
                    }

                }
            }
        });
    }
//--------------------------------------------------------------------------------------------------------------------------

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
//                        firebaseDatabaseRef.userStatusRef.setValue("Online");
                        startActivity(new Intent(Activity1_login.this, Activity4_home.class));
                        finish();
                        loadingDialog.dismiss();

                    } else {
                        Toast.makeText(Activity1_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyCode (String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(storedVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    public void sendVerificationCode(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)            // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS)      // Timeout duration
                .setActivity(this)                      // Activity (for callback binding)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

    //                            Log.e("verification Failed",e.getMessage());
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(Activity1_login.this, "Invalid request", Toast.LENGTH_SHORT).show();
                        }
                        else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(Activity1_login.this, "The SMS quota for the project", Toast.LENGTH_SHORT).show();
                        }
                        else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                            Toast.makeText(Activity1_login.this, "reCAPTCHA verification attempted with null Activity", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Activity1_login.this, "verification Failed111111", Toast.LENGTH_SHORT).show();
                            Log.e("verification Failed",e.getMessage());
                        }
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        storedVerificationId = verificationId;
                        resendToken = token;

                        Toast.makeText(Activity1_login.this,"OTP send to "+phoneNumber, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    void EmailLogin(String EMAIL,String PASSWORD){
        if (!checkConnection.isInternetAvailable(Activity1_login.this)) {
            alertDailog.setTitle("No Internet Connectoin")
                    .setMessage("Please check your internet connection and try again")
                    .setNegativeButton("Try Again", null)
                    .show();
        }else {
            loadingDialog.show();
            firebaseAuth.signInWithEmailAndPassword(EMAIL, PASSWORD)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                currentUser = firebaseAuth.getCurrentUser();
                                if (currentUser != null) {
                                    if (currentUser.isEmailVerified()) {
                                        startActivity(new Intent(Activity1_login.this, MainActivity.class));
                                        finish();
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity1_login.this);
                                        builder.setTitle("Verify Email!");
                                        builder.setMessage("Your email address is not verified. Please check your inbox and verify your email before login.");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                sendEmailVerification();
                                            }
                                        });
                                        builder.setNegativeButton("SignOut", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                firebaseAuth.signOut();
                                                startActivity(new Intent(Activity1_login.this, Activity1_login.class));
                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }

                                } else {
                                    Toast.makeText(Activity1_login.this, "user not login", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                alertDailog.setTitle("User Not Found!")
                                        .setMessage("Email or Password is incorrect.\nPlease enter correct credentials")
                                        .setPositiveButton("Signin", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(Activity1_login.this, Activity2_signin.class));
                                                finish();
                                            }
                                        })
                                        .setNegativeButton("Try Again", null)
                                        .show();
                                edit2.setText("");
                            }
                            loadingDialog.dismiss();
                        }

                    });
        }
    }
    void sendEmailVerification() {
            currentUser.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity1_login.this,"Verification email sent to\n"+currentUser.getEmail(), Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                        } else {
                            Toast.makeText(Activity1_login.this,"Failed to send verification email", Toast.LENGTH_LONG).show();
                            firebaseAuth.signOut();
                        }
                    });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Auto-click the button when activity starts
        Email.performClick();
    }
}