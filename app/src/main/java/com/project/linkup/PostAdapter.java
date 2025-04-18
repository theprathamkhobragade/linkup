package com.project.linkup;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewholder> {
    boolean alreadyliked=false;
    boolean like;

    Activity4_home activity4_home;
    ArrayList<Posts> postsArrayList;
    public PostAdapter(Activity4_home activity4_home, ArrayList<Posts> postsArrayList){
        this.activity4_home=activity4_home;
        this.postsArrayList=postsArrayList;
    }


    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new viewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Posts post=postsArrayList.get(position);

        holder.Info.setText(post.PostId);
        holder.LikeCount.setText(String.valueOf(post.Likes));
        holder.CommentCount.setText(String.valueOf(post.Comments));
        holder.Caption.setText(post.Caption);

        String postUserId=post.UserId;
        DatabaseReference postUserIdRef=FirebaseDatabase.getInstance().getReference("Users/"+postUserId);
        postUserIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userProfile = dataSnapshot.child("profileUrl").getValue(String.class);
//                    Glide.with(holder.itemView.getContext())                               //------UserProfilef
                    userProfile=userProfile + "?timestamp=" + System.currentTimeMillis();
//                    Picasso.get()
                    if (!activity4_home.isDestroyed() && !activity4_home.isFinishing()) {
                        Glide.with(holder.itemView.getContext())
                                .load(userProfile)
                                .placeholder(R.drawable.icon_profile) // Optional placeholde
                                .error(R.drawable.icon_profile) // Optional error image
                                .into(holder.userProfile);
                    }

                    String userName=dataSnapshot.child("userName").getValue(String.class);
                    holder.UserName.setText(userName);
                    holder.CaptionUsername.setText(userName);
                } else {
                    Log.e("PostAdapter!", " post uploader not found in database.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Load image using Glide
        Glide.with(holder.itemView.getContext())
                .load(post.ImageUri) // your image URL
                .placeholder(R.drawable.icon_profile) // optional: placeholder while loading
                .error(R.drawable.icon_profile)       // optional: image if loading fails
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object o, @NonNull Target<Drawable> target, boolean b) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Drawable drawable, @NonNull Object o, Target<Drawable> target, @NonNull DataSource dataSource, boolean b) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.Post);             // your target ImageView


        holder.editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Posts/"+post.PostId+"/likes");

//                holder.Username.setText(post.PostId);

                if(alreadyliked){
                    i=(post.Likes-1);           //like=True
                    holder.imgLike.setImageResource(R.drawable.icon_eye_on);
                    alreadyliked=false;
                }
                else{
                    i=(post.Likes+1);           //like=False
                    holder.imgLike.setImageResource(R.drawable.icon_eye_off);
                    alreadyliked=true;
                }
                databaseReference.setValue(i);

            }
        });
    }



    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        private ActionBarDrawerToggle toggle;
        private RelativeLayout relativeLayout;

        CircleImageView userProfile;
        ImageView Post;
        ImageView imgLike,imgComment;
        LinearLayout LayoutCaption;
        TextView editPost;
        TextView UserName,Info,LikeCount,CommentCount,CaptionUsername,Caption;
        ProgressBar progressBar;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            userProfile=itemView.findViewById(R.id.userProfile);
            Post=itemView.findViewById(R.id.post);


            UserName=itemView.findViewById(R.id.userName);
            Info=itemView.findViewById(R.id.info);
            editPost=itemView.findViewById(R.id.postEdit);
            LikeCount=itemView.findViewById(R.id.likes);
            CommentCount=itemView.findViewById(R.id.comments);
            CaptionUsername=itemView.findViewById(R.id.captionUsername);
            Caption=itemView.findViewById(R.id.caption);

            imgLike=itemView.findViewById(R.id.imgLike);
            imgComment=itemView.findViewById(R.id.imgComment);
            LayoutCaption=itemView.findViewById(R.id.layoutCaption);
            progressBar=itemView.findViewById(R.id.progressBar);

        }


    }


}
