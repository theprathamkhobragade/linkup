package com.project.linkup;

import static com.project.linkup.Activity7_messenger.SENDER_PROFILE;
import static com.project.linkup.Activity8_chatting.RECEIVER_PROFILE;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdp extends RecyclerView.Adapter<MessageAdp.holder> {
    Context context;
    ArrayList<Messages> messagesArrayList;

    public MessageAdp(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return 1; // Sent message
        } else {
            return 0; // Received message
        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(context).inflate(R.layout.activity8_sender, parent, false); // Sent message layout
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.activity8_receiver, parent, false); // Received message layout
        }
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Messages messages = messagesArrayList.get(position);
        holder.message.setText(messages.getMessage());
        holder.time.setText(messages.getTimestamp());
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isFinishing() || activity.isDestroyed()) {
                return; // Don't load image
            }
        }

        if(holder.getItemViewType()==1){

            Glide.with(context)
                    .load(SENDER_PROFILE)
                    .placeholder(R.drawable.icon_profile)
                    .error(R.drawable.icon_profile)
                    .into(holder.profile);
        }
        else {
            Glide.with(context)
                    .load(RECEIVER_PROFILE)
                    .placeholder(R.drawable.icon_profile)
                    .error(R.drawable.icon_profile)
                    .into(holder.profile);
        }

//        Glide.with(holder.itemView)
//                .load(message.getImageUrl())
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    public static class holder extends RecyclerView.ViewHolder {
        TextView message, time;
        CircleImageView senderProfile,profile;

        public holder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.senderMsg);
            time = itemView.findViewById(R.id.senderTime);
            profile = itemView.findViewById(R.id.profile);
        }
    }
}
