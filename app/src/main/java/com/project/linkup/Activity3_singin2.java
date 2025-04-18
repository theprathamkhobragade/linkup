package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity3_singin2 extends BaseActivity {
    LinearLayout layoutUsername,layoutName,layoutMobile,layoutDOB,layoutGender;
    TextView textUsername,textName,textAbout,textMobile,textDOB,textGender;
    TextView editDOB,editGender;
    EditText editUsername,editName,editAbout,editMobile;
    CircleImageView userProfile;
    RadioGroup radioGroup;

    TextView Skip,Notify;
    Button buttonResetData,buttonSaveData;

    public static File file;

    String Username,Name, About,Phone,DOB,Gender;

    String ProfileUrl;

    DatabaseReference addUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity3_signin2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);

        userProfile=findViewById(R.id.userProfile);
        layoutUsername=findViewById(R.id.layoutUsername);
        layoutName=findViewById(R.id.layoutName);
        layoutMobile=findViewById(R.id.layoutMobile);
        layoutDOB=findViewById(R.id.layoutDOB);
        layoutGender=findViewById(R.id.layoutGender);

        textUsername=findViewById(R.id.textUsername);
        textName=findViewById(R.id.textName);
        textAbout=findViewById(R.id.textAbout);
        textMobile=findViewById(R.id.textMobile);
        textDOB=findViewById(R.id.textDOB);
        textGender=findViewById(R.id.textGender);

        editUsername=findViewById(R.id.editUsername);
        editName=findViewById(R.id.editName);
        editAbout=findViewById(R.id.editAbout);
        editMobile=findViewById(R.id.editMobile);
        editDOB=findViewById(R.id.editDOB);
        editGender=findViewById(R.id.editGender);

        Skip=findViewById(R.id.skip);
        Notify=findViewById(R.id.notify);
        radioGroup = findViewById(R.id.radioGroup);
        buttonSaveData=findViewById(R.id.saveData);
        buttonResetData=findViewById(R.id.resetData);

        //-----------------------------------------------
        firebaseDatabaseRef.userProfile(new FirebaseDatabaseRef.FirebaseCallback() {
            @Override
            public void onCallback(String profileUrl,String UserName,String Name,String About,String userStatusF) {
                editUsername.setHint(UserName);
                editName.setHint(Name);
                editAbout.setHint(About);

            }
        });
        firebaseDatabaseRef.userPersonal(new FirebaseDatabaseRef.PersonalInfo() {
            @Override
            public void oncallbackPersonal(String Email, String PhoneNo, String Gender, String DOB) {
                editMobile.setHint(PhoneNo);
                editDOB.setHint(DOB);
                editGender.setHint(Gender);
            }
        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity3_singin2.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        layoutDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                // Show DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Activity3_singin2.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            editDOB.setText(selectedDate);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        layoutGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editGender.setVisibility(View.GONE);
                radioGroup.setVisibility(View.VISIBLE);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton=findViewById(checkedId);
                        if(checkedId!=-1){
                            editGender.setText(selectedRadioButton.getText());
                        }else{
                            editGender.setText(null);
                        }

                        if(editGender.getVisibility()!=View.VISIBLE){
                            editGender.setVisibility(View.VISIBLE);
                            radioGroup.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        buttonResetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get().load(R.drawable.icon_close).into(userProfile);
                file=null;
                editUsername.requestFocus();
                editUsername.setText(null);
                editName.setText(null);
                editMobile.setText(null);
                editAbout.setText(null);
                editDOB.setText(null);
                radioGroup.clearCheck();
                radioGroup.setVisibility(View.GONE);
            }
        });


        buttonSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Acitvity----------- 3",USERID);
                Username=editUsername.getText().toString();
                Name=editName.getText().toString();
                About=editAbout.getText().toString();
                Phone=editMobile.getText().toString();
                DOB=editDOB.getText().toString();
                Gender=editGender.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(Activity3_singin2.this);

                builder.setTitle("No Profile Image!");
                builder.setCancelable(false);
                builder.setMessage("You have not selects any Profile Picture");
                builder.setPositiveButton("Upload Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
                    }
                });
                builder.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addData();
                        dialog.dismiss();
                    }
                });

                if(file==null){
                    builder.create().show();
                }
                else{
                    Log.e("Acitvity upload 3",USERID);
                    String FilePath = "ProfileImg/" + USERID+".jpg";;
                    ImageUploader.uploadFile(file,FilePath);
                    addData();
                }

            }
        });
    }

    void addData(){

        if(!Username.isEmpty()){
            addUserRef.child("userName").setValue(Username);
        }
        if(!Name.isEmpty()){
            addUserRef.child("name").setValue(Name);
        }
        if(!About.isEmpty()){
            addUserRef.child("about").setValue(About);
        }
        if(!Phone.isEmpty()){
            addUserRef.child("phoneNo").setValue(Phone);
        }
        if(!DOB.isEmpty()){
            addUserRef.child("dob").setValue(DOB);
        }
        if(!Gender.isEmpty()){
            addUserRef.child("gender").setValue(Gender);
        }

        Intent intent=new Intent(Activity3_singin2.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //-----image picker Activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    userProfile.setImageURI(selectedImageUri);
                    file=ImageUploader.getFileFromUri(this,selectedImageUri);

                }
            }
        }
    }
}