package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.postRef;
import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity8_profile extends BaseActivity {

    String userName,name,about;

    CircleImageView UserProfile;
    TextView textUserName,textName,textAbout;
    Button buttonEditProfile;

    ImageView footerHome,footerSearch,footerCreatePost,footerMessenger;
    CircleImageView footerUserProfile;

    GridView gridView;
    GridAdapter adapter;
    List<GridItem> itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity8_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
//----------footer.xml
        View footerView = findViewById(R.id.footer);
        footerHome=footerView.findViewById(R.id.home);
        footerSearch=footerView.findViewById(R.id.search);
        footerCreatePost=footerView.findViewById(R.id.createPost);
        footerMessenger=footerView.findViewById(R.id.messenger);
        footerUserProfile=footerView.findViewById(R.id.userProfile);
//----->
        UserProfile=findViewById(R.id.userprofile);
        textUserName=findViewById(R.id.username);
        textName=findViewById(R.id.name);
        textAbout=findViewById(R.id.about);
        buttonEditProfile=findViewById(R.id.editprofile);

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity8_profile.this,Activity9_editprofile.class);
                startActivity(intent);
            }
        });



        firebaseDatabaseRef.userProfile(new FirebaseDatabaseRef.FirebaseCallback() {
            @Override
            public void onCallback(String ProfileUrl,String UserName,String Name,String About,String Status) {

                if (!Activity8_profile.this.isDestroyed() && !Activity8_profile.this.isFinishing()) {
//                    Glide.with(Activity8_profile.this)
                    ProfileUrl=ProfileUrl + "?timestamp=" + System.currentTimeMillis();
                    Picasso.get()
                            .load(ProfileUrl)
                            .placeholder(R.drawable.icon_profile)
                            .error(R.drawable.icon_profile)
                            .into(UserProfile);


                    textUserName.setText(UserName);
                    textName.setText(Name);
                    textAbout.setText(About);

//                    Glide.with(Activity8_profile.this)
                    Picasso.get()
                            .load(ProfileUrl)
                            .placeholder(R.drawable.icon_profile)
                            .error(R.drawable.icon_profile)
                            .into(footerUserProfile);
                }
            }
        });

        gridItems();
        footer();
    }//------------------------------------------------------>>>>>

    private void gridItems(){                   //------------------gridItems()
        gridView = findViewById(R.id.gridview);
        itemList = new ArrayList<>();

        adapter = new GridAdapter(this, itemList);
        gridView.setAdapter(adapter);

        postRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear(); // Clear list to prevent duplicates
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GridItem gridItem = dataSnapshot.getValue(GridItem.class);
                    if (gridItem != null && gridItem.getUserId().equals(USERID) ) { // Ensure we don't add null objects
                        itemList.add(gridItem);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter after updating data

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
//----------footer
    public void footer(){
        footerHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity8_profile.this,Activity4_home.class);
                startActivity(intent);
            }
        });
        footerCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity8_profile.this,Activity6_createpost.class);
                startActivity(intent);
            }
        });
        footerMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity8_profile.this,Activity7_messenger.class);
                startActivity(intent);
            }
        });
        footerUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity
                startActivity(getIntent()); // Restart activity
            }
        });
    }

}