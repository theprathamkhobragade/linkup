package com.project.linkup;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {

    Activity7_messenger activity7_messenger;
    ArrayList<Users> usersArrayList;
    public UserAdapter(Activity7_messenger activity7_messenger, ArrayList<Users> usersArrayList) {
        this.activity7_messenger=activity7_messenger;
        this.usersArrayList=usersArrayList;
    }
    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(activity7_messenger).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        Users user=usersArrayList.get(position);
        holder.Username.setText(user.UserName);
        holder.Userstatus.setText(user.Status);
        if(user.Status.equals("Online")){
            holder.StatusIcon.setImageResource(R.drawable.icon_online);
        }else{
            holder.StatusIcon.setImageResource(R.drawable.icon_offline);
        }

//        Glide.with(holder.itemView.getContext())
            String profileUrl=user.ProfileUrl + "?timestamp=" + System.currentTimeMillis();
                Picasso.get()
                .load(profileUrl)
                .placeholder(R.drawable.icon_profile)
                .error(R.drawable.icon_profile)
                .into(holder.ReciverProfile);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity7_messenger, Activity8_chatting.class);
                intent.putExtra("RECEIVER_NAME",user.getUserName());
                intent.putExtra("RECEIVER_ID",user.getUserId());
                intent.putExtra("RECEIVER_STATUS",user.getStatus());
                intent.putExtra("RECEIVER_PROFILE",user.getProfileUrl());
                activity7_messenger.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView Username,Userstatus;
        CircleImageView ReciverProfile;
        ImageView StatusIcon;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            Username=itemView.findViewById(R.id.username);
            Userstatus=itemView.findViewById(R.id.userstatus);
            ReciverProfile=itemView.findViewById(R.id.reciverProfile);
            StatusIcon=itemView.findViewById(R.id.statusIcon);

        }
    }

}
