package com.example.aapdaseva;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessageList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private EditText messageEditText;
    private ImageButton sendButton;
    private String currentEmail; // Email of the current user
    private String receiverEmail; // Email of the receiver
    private String name;

    TextView agencyName;
    ImageButton btnBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        chatRecyclerView = findViewById(R.id.recyclerViewChat);
        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);

        messageEditText = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.buttonSend);
        agencyName = findViewById(R.id.txtAgencyName);

        Intent intent = getIntent();
        receiverEmail = intent.getStringExtra("email");
        String name = intent.getStringExtra("agencyname");
        agencyName.setText(name);


        btnBack=findViewById(R.id.btn_chatBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iback = new Intent(getApplicationContext(), dashboard.class);
                startActivity(iback);
                finish();
            }
        });

        currentEmail = mAuth.getCurrentUser().getEmail(); // Get current user's email

        // Retrieve receiver's email from the intent (You can pass this from the previous activity)

        fetchChatMessages( );

        // Handle send button click
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        // Initialize chat message list
        chatMessageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessageList, currentEmail);

        chatRecyclerView.setAdapter(chatAdapter);


        // Enable the send button when there's text in the message edit text
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sendButton.setEnabled(charSequence.toString().trim().length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void fetchChatMessages() {
        // Query to fetch chat messages
        Query query = db.collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereEqualTo("senderId", currentEmail)
                .whereEqualTo("receiverId", receiverEmail)
                .limit(50);

        Query query2 = db.collection("chats")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .whereEqualTo("senderId", receiverEmail)
                .whereEqualTo("receiverId", currentEmail)
                .limit(50);

        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        tasks.add(query.get());
        tasks.add(query2.get());

        // Use Tasks.whenAllSuccess to execute both queries and merge the results
        Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
            @Override
            public void onSuccess(List<Object> results) {
                List<ChatMessage> mergedMessages = new ArrayList<>();

                for (Object result : results) {
                    QuerySnapshot snapshot = (QuerySnapshot) result;
                    for (DocumentChange dc : snapshot.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            ChatMessage chatMessage = dc.getDocument().toObject(ChatMessage.class);
                            mergedMessages.add(chatMessage);
                        }
                    }
                }

                // Sort the merged messages by timestamp
                Collections.sort(mergedMessages, (message1, message2) ->
                        message1.getTimestamp().compareTo(message2.getTimestamp())
                );


                chatMessageList.addAll(mergedMessages);
                chatAdapter.notifyDataSetChanged();
                scrollToBottom();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle query failure
                Log.e(TAG, "Error fetching chat messages: " + e.getMessage());
            }
        });
    }


    private void sendMessage() {
        String messageText = messageEditText.getText().toString().trim();

        if (!messageText.isEmpty()) {
            // Create a new chat message
            ChatMessage chatMessage = new ChatMessage(
                    messageText,
                    currentEmail,
                    receiverEmail, // Use receiver's email
                    new Date()
            );
            chatMessage.setMessage(messageText);


            chatMessageList.add(chatMessage);
            chatAdapter.notifyItemInserted(chatMessageList.size()-1 );


            // Save the message to Firestore
            db.collection("chats").add(chatMessage)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                // Clear the message edit text
                                messageEditText.setText("");
                            } else {
                                Log.e(TAG, "Error sending message: " + task.getException().getMessage());
                                // Handle error
                            }
                        }
                    });

            messageEditText.setText("");



        }
    }

    private void scrollToBottom() {
        chatRecyclerView.scrollToPosition(chatAdapter.getItemCount() );
    }
}
