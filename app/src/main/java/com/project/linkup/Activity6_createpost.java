package com.project.linkup;

import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.FirebaseDatabaseRef.postRef;
import static com.project.linkup.FirebaseDatabaseRef.userRef;
import static com.project.linkup.MainActivity.currentUser;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class Activity6_createpost extends BaseActivity {
    ImageView PostImage;
    EditText editCaption;
    Button buttonCancel,ButtonPost;

    public static String FilePath,POSTID;
    public static File file;
    public static String postFileName;

    int postCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity6_createpost);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
        postCount();

        editCaption=findViewById(R.id.editCaption);
        buttonCancel=findViewById(R.id.buttonCancel);
        ButtonPost=findViewById(R.id.buttonPost);
        PostImage=findViewById(R.id.postImage);

        PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Activity6_createpost.this, Activity4_home.class);
                startActivity(i);
                finish();
            }
        });
        ButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(file!=null){
                    ImageUploader.uploadFile(file,FilePath);
                    addToDatabase();
                }else {
                    Toast.makeText(Activity6_createpost.this, "Select Image to upload!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//------------------------------------------------------------------------------------------------------------

    void postCount(){
        userRef.child(USERID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    postCount = dataSnapshot.child("postCount").getValue(int.class);
                }
                else {
                    postCount=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                postCount=0;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //-----image picker Activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    PostImage.setImageURI(selectedImageUri);

                    String FileName = USERID + "_post"+(postCount+1)+".jpg";
                    FilePath = "Posts" + "/" + FileName;

                    file=ImageUploader.getFileFromUri(this,selectedImageUri);
                }
            }
        }
    }

    private void addToDatabase(){
        int Likes=0,Comments=0;
        String Caption=editCaption.getText().toString();

        String PostUrl="https://ligptkvwcwnwoyogyodf.supabase.co/storage/v1/object/linkup/"+FilePath;

        // Generate a unique key
        POSTID = postRef.push().getKey();

        // Create your post object
        Posts post= new Posts(POSTID,USERID,PostUrl,Caption,Likes,Comments);

        postRef.child(POSTID).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    userRef.child(USERID).child("postCount").setValue(postCount+1);
                    Intent i=new Intent(Activity6_createpost.this, Activity4_home.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i=new Intent(Activity6_createpost.this, Activity4_home.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }
}