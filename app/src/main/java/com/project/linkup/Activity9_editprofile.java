package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;
import static com.project.linkup.MainActivity.firebaseAuth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity9_editprofile extends BaseActivity {

    CircleImageView userProfile,Camera;
    String userProfileUrl;

    LinearLayout layoutUsername,layoutName,layoutAbout;
    TextView textUsername,textName,textAbout, PersonalInfo;
    TextView editUsername,editName,editAbout;
    Button buttonLogout;

    Boolean clickedProfile, clickedUsername,clickedName,clickedAbout;

    View dialogView;
    TextView text1;
    EditText edit1;
    AlertDialog.Builder builder;

    ImageView dialogProfile;
    TextView profileImageName;
    Uri selectedImageUri;
    String FilePath;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity9_editprofile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);


        userProfile=findViewById(R.id.userProfile);
//        Camera=findViewById(R.id.camera);
        layoutUsername=findViewById(R.id.layoutUsername);
        layoutName=findViewById(R.id.layoutName);
        layoutAbout=findViewById(R.id.layoutAbout);

        textUsername=findViewById(R.id.textUsername);
        textName=findViewById(R.id.textName);
        textAbout=findViewById(R.id.textAbout);
        PersonalInfo=findViewById(R.id.personalInfo);

        editUsername=findViewById(R.id.editUsername);
        editName=findViewById(R.id.editName);
        editAbout=findViewById(R.id.editAbout);
        buttonLogout=findViewById(R.id.buttonLogout);

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_editdata, null);
        text1 = dialogView.findViewById(R.id.text1);
        edit1 = dialogView.findViewById(R.id.edit1);
        builder = new AlertDialog.Builder(Activity9_editprofile.this);

        firebaseDatabaseRef.userProfile(new FirebaseDatabaseRef.FirebaseCallback() {
            @Override
            public void onCallback(String profileUrl,String userName,String Name, String About,String userStatus) {

                if (!Activity9_editprofile.this.isDestroyed() && !Activity9_editprofile.this.isFinishing()) {
//                    Glide.with(Activity9_editprofile.this)
                    userProfileUrl=profileUrl + "?timestamp=" + System.currentTimeMillis();
                    Picasso.get()
                            .load(userProfileUrl)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
                            .error(R.drawable.icon_profile)
                            .into(userProfile);
                }

                    editUsername.setText(userName);
                    editName.setText(Name);
                    editAbout.setText(About);

            }
        });

        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedUsername=false;clickedName=false;clickedAbout=false;clickedProfile=true;

                profileDialog();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        layoutUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TEXT1,EDIT1;
                clickedUsername=true;clickedName=false;clickedAbout=false;clickedProfile=false;
                layoutClicable(false);

                TEXT1=textUsername.getText().toString();
                Dialog(TEXT1);
            }
        });
        layoutName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TEXT1,EDIT1;
                clickedName=true;clickedUsername=false;clickedAbout=false;clickedProfile=false;
                layoutClicable(false);

                TEXT1=textName.getText().toString();
                Dialog(TEXT1);
            }
        });
        layoutAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String TEXT1,EDIT1;
                clickedAbout=true;clickedName=false;clickedUsername=false;clickedProfile=false;
                layoutClicable(false);

                TEXT1=textAbout.getText().toString();
                Dialog(TEXT1);
            }
        });
        PersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity9_editprofile.this, Activity10_editpersonalinfo.class));
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseDatabaseRef.userStatus("Offline");

                FirebaseAuth.getInstance().signOut();
                // Clear stack and go to MainActivity (Splash or Login)
                Intent intent = new Intent(Activity9_editprofile.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }//-----------------------------------------------------------------------------------------------
    @SuppressLint("MissingInflatedId")
    void profileDialog(){
        LayoutInflater profileInflater = getLayoutInflater();
        View profileDialog = profileInflater.inflate(R.layout.dialog_profile, null);
        dialogProfile=profileDialog.findViewById(R.id.profileImage);
        profileImageName=profileDialog.findViewById(R.id.profileImageName);
        profileImageName.setVisibility(View.INVISIBLE);
//        dialogProfile.setImageResource(R.drawable.icon_profile);
        Picasso.get()
                .load(userProfileUrl)
                .placeholder(R.drawable.icon_profile)
                .error(R.drawable.icon_profile)
                .into(dialogProfile);

        dialogProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity9_editprofile.this);
        builder.setView(profileDialog);
        builder.setTitle("Profile");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File file=ImageUploader.getFileFromUri(Activity9_editprofile.this,selectedImageUri);

                ImageUploader.checkExistImage(userProfileUrl,file,FilePath);
                layoutClicable(true);
            }
        });
        builder.show();
    }
    void Dialog(String TEXT1){
        // Remove from parent if already attached
        ViewGroup parent = (ViewGroup) dialogView.getParent();
        if (parent != null) {
            parent.removeView(dialogView);
        }
        text1.setText("New "+TEXT1);
        edit1.setText(null);
        builder.setView(dialogView);
        builder.setTitle("Change "+TEXT1);
        builder.setCancelable(false);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEdit1 = edit1.getText().toString();
                saveNewData(newEdit1);
                layoutClicable(true);
                edit1.setText(null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                layoutClicable(true);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    void saveNewData(String newEdit1){
        if(clickedUsername){
            userRef.child("userName").setValue(newEdit1);
        }
        if(clickedName){
            userRef.child("name").setValue(newEdit1);
        }
        if(clickedAbout){
            userRef.child("about").setValue(newEdit1);
        }
    }

    void layoutClicable(boolean value){
        userProfile.setClickable(value);
        layoutUsername.setClickable(value);
        layoutName.setClickable(value);
        layoutAbout.setClickable(value);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //-----image picker Activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data != null) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    String FileName = USERID + ".jpg";
                    FilePath = "ProfileImg" + "/" + FileName;;
                    dialogProfile.setImageURI(selectedImageUri);
                    profileImageName.setText(FileName);
                    profileImageName.setVisibility(View.VISIBLE);

                }
            }
        }
    }

}