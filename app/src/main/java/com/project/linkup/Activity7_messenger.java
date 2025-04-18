package com.project.linkup;

import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity7_messenger extends BaseActivity {
    public static String SENDER_PROFILE;

    RecyclerView userRecyclerView;
    ArrayList<Users> usersArrayList;
    UserAdapter userAdapter;

    ImageView footerHome,footerSearch,footerCreatePost,footerMessenger;
    CircleImageView footerUserProfile;

    DatabaseReference allUsersRf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity7_messenger);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);

        userRecyclerView =findViewById(R.id.usersRecyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//----------footer.xml
        View footerView = findViewById(R.id.footer);
        footerHome=footerView.findViewById(R.id.home);
        footerSearch=footerView.findViewById(R.id.search);
        footerCreatePost=footerView.findViewById(R.id.createPost);
        footerMessenger=footerView.findViewById(R.id.messenger);
        footerUserProfile=footerView.findViewById(R.id.userProfile);

//-----FirebaseRef
        firebaseDatabaseRef.userProfile(new FirebaseDatabaseRef.FirebaseCallback() {
            @Override
            public void onCallback(String profileUrl,String UserName,String Name,String About,String userStatusF) {
                SENDER_PROFILE=profileUrl;
                if (!Activity7_messenger.this.isDestroyed() && !Activity7_messenger.this.isFinishing()) {
                    profileUrl=profileUrl + "?timestamp=" + System.currentTimeMillis();
                    Picasso.get().load(profileUrl)
                            .placeholder(R.drawable.icon_profile)
                            .error(R.drawable.icon_profile)
                            .into(footerUserProfile);
                }
            }
        });

        userList();
        footer();

    }//--------------------------------------------------------------------------------------------------------------
    private void userList(){
        usersArrayList= new ArrayList<>();

        allUsersRf= FirebaseDatabase.getInstance().getReference().child("Users");
        allUsersRf.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users= dataSnapshot.getValue(Users.class);
                    assert users != null;
                    if(!USERID.equals(users.UserId)){
                        usersArrayList.add(users);
                    }
                }
                userAdapter =new UserAdapter(Activity7_messenger.this, usersArrayList);
                userAdapter.notifyDataSetChanged();
                userRecyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    //-----Foother
    public void footer(){
        footerHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UnsafeIntentLaunch")
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity7_messenger.this,Activity4_home.class);
                startActivity(intent);
            }
        });
        footerCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity7_messenger.this,Activity6_createpost.class);
                startActivity(intent);
            }
        });
        footerMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish(); // Close current activity
                startActivity(getIntent()); // Restart activity

            }
        });
        footerUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity7_messenger.this,Activity8_profile.class);
                startActivity(intent);
            }
        });
    }
}