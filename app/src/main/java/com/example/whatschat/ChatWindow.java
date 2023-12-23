package com.example.whatschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatschat.databinding.ActivityChatWindowBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatWindow extends AppCompatActivity {

    ActivityChatWindowBinding mainBinding;
    String recieverImg, recieverName, recieverUid, senderUid;
    FirebaseDatabase database;
    FirebaseAuth auth;
    public static String senderImg;
    public static String recieverImage;
    String senderRoom, recieverRoom;
    ArrayList<MsgModelClass> messageArraylist;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityChatWindowBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        recieverName = getIntent().getStringExtra("nameee");
        recieverUid = getIntent().getStringExtra("uid");
        recieverImg = getIntent().getStringExtra("usserImg");

        mainBinding.profileName.setText("" + recieverName);
        Picasso.get().load(recieverImg).into(mainBinding.profileImage);

        //=============================================================================================
        //RecyclerView work

        messageArraylist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mainBinding.messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatWindow.this, messageArraylist);
        mainBinding.messageRecyclerView.setAdapter(messageAdapter);


        //=============================================================================================
        //code for Send and Recieve data in firebase

        senderUid = auth.getUid();

        senderRoom = senderUid +"->"+ recieverUid;
        recieverRoom = recieverUid+"->"+ senderUid;

        DatabaseReference reference = database.getReference().child("User").child(auth.getUid());
        DatabaseReference chatreference = database.getReference().child("chats").child(senderRoom).child("messages");

        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArraylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    MsgModelClass msg = new MsgModelClass();
//                    String messagepack = dataSnapshot.getValue().toString();
//                    msg.setMessage(messagepack);
                      MsgModelClass msg= dataSnapshot.getValue(MsgModelClass.class);
                     messageArraylist.add(msg);

                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UsserGettersetter pack = snapshot.getValue(UsserGettersetter.class);

                assert pack != null;
                senderImg = pack.getImageuri();
                recieverImage = recieverImg;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mainBinding.sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mainBinding.messageText.getText().toString();
                if (message.isEmpty()) {
                    Toast.makeText(ChatWindow.this, "TextField is Empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mainBinding.messageText.setText("");
                    Date date = new Date();
                    MsgModelClass MessagePackk = new MsgModelClass(message, senderUid, date.getTime());

                    database = FirebaseDatabase.getInstance();
                    database.getReference().child("chats").child(senderRoom).child("messages")
                            .push().setValue(MessagePackk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    database.getReference().child("chats").child(recieverRoom).child("messages")
                                            .push().setValue(MessagePackk).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                }
                                            });
                                }
                            });

                }
            }
        });

    }
}