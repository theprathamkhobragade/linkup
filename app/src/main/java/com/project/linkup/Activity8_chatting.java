package com.project.linkup;

import static com.project.linkup.FirebaseDatabaseRef.chatRef;
import static com.project.linkup.MainActivity.USERID;
import static com.project.linkup.MainActivity.currentUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity8_chatting extends BaseActivity {

    public String SENDER_ID,RECEIVER_ID,RECEIVER_NAME,RECEIVER_STATUS;
    public static String  RECEIVER_PROFILE;
    TextView textRecieverName,textRecieverStatus;
    EditText editMessage;
    CircleImageView receiverProfile,buttonSend;
    ImageView back;

    RecyclerView messageRecyclerView;
    MessageAdp messageAdapter;
    ArrayList<Messages> messagesArrayList;

    FirebaseAuth firebaseAuth;
    DatabaseReference receiverRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity8_chatting);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        overridePendingTransition(R.anim.blinkin,R.anim.blinkin);
        SENDER_ID= USERID;


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Firebase Initialization
        firebaseAuth = FirebaseAuth.getInstance();


        // UI Elements
        receiverProfile=findViewById(R.id.receiverProfile);
        textRecieverName = findViewById(R.id.receiverUsername);
        textRecieverStatus=findViewById(R.id.receiverStatus);
        editMessage = findViewById(R.id.typeMessage);
        buttonSend = findViewById(R.id.sendbtn);
        messageRecyclerView = findViewById(R.id.messagesRecyclerView);

        // Get Intent Data
        RECEIVER_ID = getIntent().getStringExtra("RECEIVER_ID");

        findViewById(android.R.id.content).getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> {
                    Rect r = new Rect();
                    findViewById(android.R.id.content).getWindowVisibleDisplayFrame(r);

                    int screenHeight = findViewById(android.R.id.content).getRootView().getHeight();
                    int keypadHeight = screenHeight - r.bottom;

                    if (keypadHeight > screenHeight * 0.15) {
                        // Keyboard is visible
                        messageRecyclerView.post(() -> {
                            messageRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                        });
                    }
                });

        receiverRef=FirebaseDatabase.getInstance().getReference("Users/"+RECEIVER_ID);
        ReciverProfile(new ReceiverInfo() {
            @Override
            public void onCallback(String ProfileUrl, String Name, String Status) {
                RECEIVER_PROFILE=ProfileUrl;
                if (!Activity8_chatting.this.isDestroyed() && !Activity8_chatting.this.isFinishing()) {
                    ProfileUrl=ProfileUrl + "?timestamp=" + System.currentTimeMillis();

//                    Glide.with(Activity8_chatting.this)
                    Picasso.get()
                            .load(ProfileUrl)
                            .placeholder(R.drawable.icon_profile)
                            .error(R.drawable.icon_profile)
                            .into(receiverProfile);
                }

                textRecieverName.setText(Name);
                textRecieverStatus.setText(Status);
            }
        });

        // Set up RecyclerView
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesArrayList = new ArrayList<>();
        messageAdapter = new MessageAdp(Activity8_chatting.this, messagesArrayList);
        messageRecyclerView.setAdapter(messageAdapter);


        buttonSend.setOnClickListener(v -> {
            String messageText = editMessage.getText().toString().trim();
            if (messageText.isEmpty()) {
                Toast.makeText(Activity8_chatting.this, "Please write something", Toast.LENGTH_LONG).show();
            } else {
                sendMessage(messageText);
            }
            editMessage.setText("");
        });
        // Load chat messages
        chatMessage();

    }//--------------------------------------------------------------------------------------------->>>>>>>>

    public interface ReceiverInfo {
        void onCallback(String ProfileUrl,String Name,String Status);
    }
    void ReciverProfile(ReceiverInfo ReciverCallback){
        receiverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ProlifeUrl,Name,Status;

                    ProlifeUrl = snapshot.child("profileUrl").getValue(String.class);
                    Name = snapshot.child("name").getValue(String.class);
                    Status=snapshot.child("status").getValue(String.class);
                    Status = (Status != null) ? Status : "Offline"; // Default to false if null

                    ReciverCallback.onCallback(ProlifeUrl,Name,Status);
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void sendMessage(final String messageText) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String timestamp = dtf.format(now);
            Messages message = new Messages(messageText, SENDER_ID, RECEIVER_ID, timestamp);

//            chatRef.push().setValue(message);
            chatRef.push().setValue(message)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Activity8_chatting.this, "Sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Activity8_chatting.this, "Message sending failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(Activity8_chatting.this, "Error sending message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chatMessage() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Messages> newMessagesList = new ArrayList<>();

                Log.d(USERID,RECEIVER_ID);
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Messages message = dataSnapshot.getValue(Messages.class);

                    if (message != null &&
                            ((message.getSenderId().equals(USERID) && message.getReceiverId().equals(RECEIVER_ID)) ||
                                    (message.getSenderId().equals(RECEIVER_ID) && message.getReceiverId().equals(USERID)))) {
                        newMessagesList.add(message);
                    }
                }
//                // Update RecyclerView only if messages changed
                if (!messagesArrayList.equals(newMessagesList)) {
                    messagesArrayList.clear();
                    messagesArrayList.addAll(newMessagesList);
                    messageAdapter.notifyDataSetChanged();

//                    // Scroll only if the latest message is sent by the user
                    if (!messagesArrayList.isEmpty() &&
                            messagesArrayList.get(messagesArrayList.size() - 1).getSenderId().equals(SENDER_ID)) {
                        messageRecyclerView.scrollToPosition(messagesArrayList.size() - 1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}