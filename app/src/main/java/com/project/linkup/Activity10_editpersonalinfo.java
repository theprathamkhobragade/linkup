package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseAuth;

import static com.project.linkup.MainActivity.firebaseDatabaseRef;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Activity10_editpersonalinfo extends BaseActivity {
    LinearLayout layoutEmail, layoutPhone, layoutDOB, layoutGender, layoutPassword;
    TextView textEmail, textPhone, textDOB, textGender, textPassword;
    TextView editEmail, editPhone, editDOB, editGender, editPassword;

    RadioGroup radioGroup;
    Boolean clickedEmail, clickedPhone, clickedGender, clickedDOB, clickedPassword;

    int STOP_AFTER = 300000; // 5 minutes (300,000 ms)
    long startTime;
    Handler handler = new Handler();
    Runnable checkEmailRunnable;

    String storedVerificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;


    View dialogView;
    TextView text1;
    EditText edit1;
    AlertDialog.Builder builder;

    String curentEmail,currentPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity10_editpersonalinfo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPhone = findViewById(R.id.layoutPhone);
        layoutDOB = findViewById(R.id.layoutDOB);
        layoutGender = findViewById(R.id.layoutGender);
        layoutPassword = findViewById(R.id.layoutPassword);

        textEmail = findViewById(R.id.textEmail);
        textPhone = findViewById(R.id.textPhone);
        textDOB = findViewById(R.id.textDOB);
        textGender = findViewById(R.id.textGender);
        textPassword = findViewById(R.id.textPassword);

        editEmail = findViewById(R.id.editEmail);
        editPhone = findViewById(R.id.editPhone);
        editDOB = findViewById(R.id.editDOB);
        editGender = findViewById(R.id.editGender);
        editPassword = findViewById(R.id.editPassword);



        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_activity10, null);
        text1 = dialogView.findViewById(R.id.text1);
        edit1 = dialogView.findViewById(R.id.edit1);
        radioGroup=dialogView.findViewById(R.id.radioGroup);
        builder = new AlertDialog.Builder(Activity10_editpersonalinfo.this);

//-----FirebaseRef
        firebaseDatabaseRef.userPersonal(new FirebaseDatabaseRef.PersonalInfo() {
            @Override
            public void oncallbackPersonal(String Email, String PhoneNo, String Gender, String DOB) {
                editPhone.setText(PhoneNo);
                editGender.setText(Gender);
                editDOB.setText(DOB);
            }
        });

        FirebaseAuthentication firebaseAuthentication = new FirebaseAuthentication();
        firebaseAuthentication.getEmail(new FirebaseAuthentication.ChangedEmailCallback() {
            @Override
            public void onCallbackEmail(String Email) {
                editEmail.setText(Email);
            }
        });
//----->>>

        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedEmail = true; clickedPhone = false; clickedGender = false; clickedDOB = false; clickedPassword = false;
                layoutClickable(false);
                String TEXT1=textEmail.getText().toString();
                Dialog(TEXT1,"Verify");

            }
        });
        layoutPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPhone = true; clickedEmail = false; clickedGender = false; clickedDOB = false; clickedPassword = false;
                layoutClickable(false);
                String TEXT1=textPhone.getText().toString();
                Dialog(TEXT1,"Verify");

            }
        });
        layoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedGender = true; clickedPhone = false; clickedEmail = false; clickedDOB = false; clickedPassword = false;
                layoutClickable(false);
                radioGroup.setVisibility(View.VISIBLE);
                String TEXT1=textGender.getText().toString();
                Dialog(TEXT1,"Save");

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton=dialogView.findViewById(checkedId);
                        edit1.setText(selectedRadioButton.getText());
                    }
                });
            }
        });
        layoutDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                int year,month,day;
                Date date;
                String DOB;

                clickedDOB = true; clickedPhone = false; clickedGender = false; clickedEmail = false; clickedPassword = false;
                layoutClickable(false);
                DOB=editDOB.getText().toString();
                if(DOB.isEmpty()){
                    DOB="05/09/2002";
                }
                SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Calendar calendar = Calendar.getInstance();
                try {
                    date=sdf.parse(DOB);
                    calendar.setTime(date);

                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    datePickerDialog = new DatePickerDialog(Activity10_editpersonalinfo.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//                                editDOB.setText(selectedDate);
                                userRef.child("dob").setValue(selectedDate);
                                layoutClickable(true);
                            }, year, month, day);
                    datePickerDialog.setOnCancelListener(dialog -> {
                        layoutClickable(true);
                    });
                    datePickerDialog.show();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        layoutPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPassword = true; clickedPhone = false; clickedGender = false; clickedEmail = false; clickedDOB = false;
                layoutClickable(false);
                String TEXT1=textPassword.getText().toString();
                Dialog("Current "+TEXT1,"Verify");

            }
        });

        checkEmailRunnable = new Runnable() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - startTime < STOP_AFTER) {
                    checkEmailUpdate();                                         // Call your email update function
                    handler.postDelayed(this, 1000);               // Run again in 1s
                } else {
//                    handler.removeCallbacks(checkEmailRunnable);
                    handler.removeCallbacks(this);                          // Stop checking after 5 min
                }
            }
        };

    }//------------------------------------------------------------------------------------
    void Dialog(String TEXT1,String Save){
        // Remove from parent if already attached
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeView(dialogView);
        }
        if(clickedEmail||clickedPassword){
            text1.setText("Current "+TEXT1);
        }else{
            text1.setText(TEXT1);
        }

        edit1.setText(null);
        builder.setView(dialogView);
        builder.setTitle("Change "+TEXT1);
        builder.setCancelable(false);

        builder.setPositiveButton(Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEdit1 = edit1.getText().toString();
                if(Save.equals("Verify")){
                    reAuthenticateAndChangeEmail(editEmail.getText().toString(),newEdit1);
                }else{
                    saveData(newEdit1);
                }

                edit1.setText(null);
                radioGroup.setVisibility(View.GONE);
                layoutClickable(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                radioGroup.setVisibility(View.GONE);
                layoutClickable(true);
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    void saveData(String newEdit1){
        if(clickedEmail){
            currentUser.verifyBeforeUpdateEmail(newEdit1).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startTime = System.currentTimeMillis(); // Start timer
                    handler.post(checkEmailRunnable);
                    curentEmail=newEdit1;
                    Toast.makeText(Activity10_editpersonalinfo.this, "verification link send to\n" + newEdit1, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(clickedPassword){
            currentUser.updatePassword(newEdit1).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(Activity10_editpersonalinfo.this, "Password Saved!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(clickedGender){
            userRef.child("gender").setValue(newEdit1);
        }else{

        }

    }

    void checkEmailUpdate(){
        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    signInAgain();
                }
            });
        }
    }

    public void signInAgain() {
        firebaseAuth.signInWithEmailAndPassword(curentEmail, currentPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        finish(); // Close current activity
                        startActivity(getIntent()); // Restart activity
                    }
                    handler.removeCallbacks(checkEmailRunnable);
                });
    }

    void reAuthenticateAndChangeEmail(String Email, String password) {
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(Email, password);
            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    currentPassword=password;
                    Toast.makeText(this, "Re-authentication success!", Toast.LENGTH_SHORT).show();
                    if(clickedEmail){
                        Dialog("New"+textEmail.getText().toString(),"Save");
                    }
                    else {
                        Dialog("New"+textPassword.getText().toString(),"Save");
                    }
                } else {
                    if(clickedEmail){
                        Dialog("Current "+textPassword.getText().toString(),"Verify");
                        edit1.setError("Wrong Password!");
                    }
                    else {
                        Dialog("Current "+textPassword.getText().toString(),"Verify");
                        edit1.setError("Wrong Password!");
                    }
                }
            });
        }
    }

    void layoutClickable(boolean value){
        layoutEmail.setClickable(value);
        layoutPhone.setClickable(value);
        layoutDOB.setClickable(value);
        layoutGender.setClickable(value);
        layoutPassword.setClickable(value);
    }
}