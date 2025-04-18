package com.project.linkup;

import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseAuth;
import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;

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
import android.view.View;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Activity2_signin extends AppCompatActivity {

    TextView Email,Phone;
    LinearLayout layout1,layout2,layout3;
    TextView text1,text2,text3,textLogin;
    EditText edit1,edit2,edit3;
    Button buttonSubmit;
    Boolean clickedEmail,clickedPhone;

    String storedVerificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;


    AlertDialog.Builder alertDailog;
    Dialog loadingDialog;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity2_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
        alertDailog = new AlertDialog.Builder(Activity2_signin.this);

        loadingDialog=new Dialog(this);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Email=findViewById(R.id.Email);
        Phone=findViewById(R.id.Phone);

        layout1=findViewById(R.id.layout1);
        layout2=findViewById(R.id.layout2);
        layout3=findViewById(R.id.layout3);
        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        edit1=findViewById(R.id.edit1);
        edit2=findViewById(R.id.edit2);
        edit3=findViewById(R.id.edit3);
        textLogin=findViewById(R.id.textLogin);
        buttonSubmit=findViewById(R.id.buttonSubmit);

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPhone=false; clickedEmail=true;
                Email.setBackgroundResource(R.drawable.border1); Phone.setBackground(null);
                text1.setText("Email");
                edit1.setHint("abc@gmail.com");
                edit1.setFocusable(true);
                edit1.setFocusableInTouchMode(true);
                edit1.requestFocus();
                edit1.setInputType(InputType.TYPE_CLASS_TEXT);
                edit1.setFilters(new InputFilter.LengthFilter[]{});
                edit1.setText(null);

                layout2.setVisibility(View.VISIBLE);
                text2.setText("Password");
                edit2.setText(null);

                layout3.setVisibility(View.VISIBLE);
                text3.setText("Confirm Password");
                edit3.setText(null);
                buttonSubmit.setText("Signin");
            }
        });
        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPhone=true; clickedEmail=false;
                Phone.setBackgroundResource(R.drawable.border1); Email.setBackground(null);
                text1.setText("Phone No");
                edit1.setText(null);
                edit1.setFocusable(true);
                edit1.setFocusableInTouchMode(true);
                edit1.requestFocus();
                edit1.setHint("0000000000");
                edit1.setInputType(InputType.TYPE_CLASS_PHONE);
                edit1.setFilters(new InputFilter.LengthFilter[]{new InputFilter.LengthFilter(10)});

                layout2.setVisibility(View.GONE);
                edit2.setText(null);

                layout3.setVisibility(View.GONE);
                buttonSubmit.setText("Send OTP");
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
                        } else {
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
                    String confirmPassword=edit3.getText().toString();
                    if(Email.isEmpty() || Password.isEmpty() || confirmPassword.isEmpty()){
                        Toast.makeText(Activity2_signin.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(!isValidEmail(Email) || Password.length()<6){
                            if(!isValidEmail(Email)){
                                edit1.setError("Enter Valid Email Address!");
                            }
                            if(Password.length()<6){
                                edit2.setError("Password length should be greater than 7");
                            }
                        }
                        else {
                            if(!Password.equals(confirmPassword)){
                                edit3.setError("password must be same!");
                            }
                            else{
                                EmailSignin(Email,Password);
                            }
                        }
                    }
                }
            }
        });
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity2_signin.this, Activity1_login.class));
                finish();
            }
        });

    }
//-------------------------------------------------------------------------------------------------------------------------//
public void sendVerificationCode(String phoneNumber){
    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)            // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS)      // Timeout duration
            .setActivity(this)                      // Activity (for callback binding)
            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//                        signInWithPhoneAuthCredential(credential);
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

//                            Log.e("verification Failed",e.getMessage());
                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(Activity2_signin.this, "Invalid request", Toast.LENGTH_SHORT).show();
                    }
                    else if (e instanceof FirebaseTooManyRequestsException) {
                        Toast.makeText(Activity2_signin.this, "The SMS quota for the project", Toast.LENGTH_SHORT).show();
                    }
                    else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                        Toast.makeText(Activity2_signin.this, "reCAPTCHA verification attempted with null Activity", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    storedVerificationId = verificationId;
                    resendToken = token;

                    Toast.makeText(Activity2_signin.this, "Sending code to: " + phoneNumber, Toast.LENGTH_SHORT).show();
                }
            })
            .build();
    PhoneAuthProvider.verifyPhoneNumber(options);
}
    private void verifyCode (String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(storedVerificationId, code);
        signInWithPhoneAuthCredential(credential);
        loadingDialog.dismiss();
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){

    }

    void EmailSignin(String Email,String Password){
        if (!checkConnection.isInternetAvailable(Activity2_signin.this)) {
            alertDailog.setTitle("No Internet Connectoin")
                    .setMessage("Please check your internet connection and try again")
                    .setNegativeButton("Try Again", null)
                    .show();
        }
        else{
            loadingDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        currentUser=firebaseAuth.getCurrentUser();
                        if(currentUser!=null){
                            sendEmailVerification(currentUser);
                        }
                        String UserId = task.getResult().getUser().getUid();
                        String ProfileUrl,UserName,Name,MobileNo,Gender,DOB,Status,About;
                        int postCount;

                        ProfileUrl="https://ligptkvwcwnwoyogyodf.supabase.co/storage/v1/object/linkup/ProfileImg/"+UserId+".jpg";
                        UserName=Email.substring(0,Email.indexOf("@"));
                        Name="Linkup User";
                        MobileNo="9503418956";
                        Gender="other";
                        DOB="12/12/2012";
                        Status="Offline";
                        About="Avilable";
                        postCount=0;

                        Users user=new Users(UserId,ProfileUrl,UserName,Name,MobileNo,Gender,DOB,Status,About,postCount);
                        userRef=FirebaseDatabase.getInstance().getReference("Users").child(UserId);

                        userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                }
                            }
                        });
                    }
                    else {
                        Exception e=task.getException();
                        if (e instanceof FirebaseAuthUserCollisionException) {
                            alertDailog.setTitle("Already Registered!")
                                    .setMessage(Email+" is already registered. Please login instead or use different email address.")
                                    .setPositiveButton("LOG IN", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(Activity2_signin.this, Activity1_login.class));
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("TRY AGAIN", null)
                                    .show();
                        } else {
                            alertDailog.setTitle("Error!")
                                    .setMessage("Something went wrong!\nPlease try after some time.")
                                    .setNegativeButton("TRY AGAIN", null)
                                    .show();
                        }
                    }loadingDialog.dismiss();
                }
            });
        }
    }
    void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity2_signin.this,"Verification email sent to\n"+user.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Activity2_signin.this,"Failed to send verification email", Toast.LENGTH_LONG).show();
                        }
                        firebaseAuth.signOut();
                        startActivity(new Intent(Activity2_signin.this, MainActivity.class));
                        finish();
                    });

    }
    public Boolean isValidEmail(String Email){
        return Patterns.EMAIL_ADDRESS.matcher(Email).matches();
    }

    public Boolean isValidPassword(String Password){
        return null;
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Auto-click the button when activity starts
        Email.performClick();
    }
}