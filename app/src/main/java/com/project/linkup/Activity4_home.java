package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.postRef;
import static com.project.linkup.MainActivity.currentUser;
import static com.project.linkup.MainActivity.firebaseDatabaseRef;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity4_home extends BaseActivity {
    ImageView footerHome,footerSearch,footerCreatePost,footerMessenger;
    CircleImageView footerUserProfile;

    RecyclerView postRecyclerView;
    ArrayList<Posts> postArrayList;
    PostAdapter postAdapter;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity4_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
//-----------------------------------------------
        firebaseDatabaseRef.userProfile(new FirebaseDatabaseRef.FirebaseCallback() {
            @Override
            public void onCallback(String profileUrl,String UserName,String Name,String About,String userStatusF) {
                if (!Activity4_home.this.isDestroyed() && !Activity4_home.this.isFinishing()) {
                    Picasso.get().load(profileUrl)
                            .placeholder(R.drawable.icon_profile)
                            .error(R.drawable.icon_profile)
                            .into(footerUserProfile);
                }
            }
        });
//------------------------footer.xml
        View footerView = findViewById(R.id.footer);
        footerHome=footerView.findViewById(R.id.home);
        footerSearch=footerView.findViewById(R.id.search);
        footerCreatePost=footerView.findViewById(R.id.createPost);
        footerMessenger=footerView.findViewById(R.id.messenger);
        footerUserProfile=footerView.findViewById(R.id.userProfile);

//--------------------------------------------->>>>
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setItemViewCacheSize(20);
        postRecyclerView.setDrawingCacheEnabled(true);
        postRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        postRecyclerView.smoothScrollToPosition(0);
        postList();
        footer();
    }

//-----postList
    private void postList() {
        postArrayList = new ArrayList<>();
        postAdapter = new PostAdapter(Activity4_home.this,postArrayList);
        postRecyclerView.setAdapter(postAdapter);

        postRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    postArrayList.add(posts);
                }
                progressBar.setVisibility(View.GONE);

                postAdapter.notifyDataSetChanged();
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
                finish(); // Close current activity
                startActivity(getIntent()); // Restart activity
            }
        });
        footerCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity4_home.this,Activity6_createpost.class);
                startActivity(intent);
            }
        });
        footerMessenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity4_home.this,Activity7_messenger.class);
                startActivity(intent);
            }
        });
        footerUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Activity4_home.this,Activity8_profile.class);
                startActivity(intent);
            }
        });
    }
}