package com.knimbus.elib;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private ArrayList<ChatMessage> messageList = new ArrayList<>();
    private ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(messageList);
    static boolean active = false;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String TAG = "ChatActivity";


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            long timestamp = dataSnapshot.child("timestamp").getValue(Long.class);
            String email = dataSnapshot.child("email").getValue().toString();
            String text = dataSnapshot.child("text").getValue().toString();
            String name = dataSnapshot.child("name").getValue().toString();
            int typeId;
            if (email.equals(mAuth.getCurrentUser().getEmail())) typeId = 0;
            else typeId = 1;
            messageList.add(new ChatMessage(text, email, timestamp, typeId, name));
            Log.d(TAG, "onChildAdded: text " + text);
            chatMessageAdapter.notifyItemInserted(messageList.size() - 1);
            rvMessages.scrollToPosition(messageList.size() - 1);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ChatRoom");

        mAuth = FirebaseAuth.getInstance();

        rvMessages = findViewById(R.id.rv_messages);
        ImageButton ibSend = findViewById(R.id.ib_assistant_testing_send);
        etMessage = findViewById(R.id.et_assistant_testing_message);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        final DatabaseReference messageReference = FirebaseDatabase.getInstance().getReference().child("messages");
        messageReference.limitToLast(30).addChildEventListener(childEventListener);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.setItemAnimator(new DefaultItemAnimator());
        rvMessages.setAdapter(chatMessageAdapter);

        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etMessage.getText().toString().trim();
                if (!msg.isEmpty()) {
                    etMessage.setText("");
                    HashMap<String, Object> childMap = new HashMap<>();
                    String key = messageReference.push().getKey();
                    childMap.put("email", user.getEmail());
                    childMap.put("name", user.getDisplayName());
                    childMap.put("timestamp", System.currentTimeMillis());
                    childMap.put("text", msg);
                    messageReference.child(key).updateChildren(childMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(ChatActivity.this, "Database error: " + databaseError, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
        //Todo: Notification manager
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }


}
