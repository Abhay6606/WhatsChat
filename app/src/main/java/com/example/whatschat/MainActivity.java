package com.example.whatschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.whatschat.databinding.ActivityLoginBinding;
import com.example.whatschat.databinding.ActivityMainBinding;
import com.example.whatschat.databinding.DialogLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String userUid;
    ArrayList<UsserGettersetter> userArrayList;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

//==================================================================================================
        //Sheared peference for Automatic login

        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit().putBoolean("flag", true);
        editor.apply();

//==================================================================================================
        //user RecylerView

        DatabaseReference reference = database.getReference().child("User");
        userArrayList = new ArrayList<>();
        userUid = auth.getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (userUid.equals(dataSnapshot.getValue(UsserGettersetter.class).userId)) {
                   }
                     else {
                        UsserGettersetter pack = dataSnapshot.getValue(UsserGettersetter.class);
                        userArrayList.add(pack);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mainBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(MainActivity.this, userArrayList);
        mainBinding.recyclerView.setAdapter(adapter);

        //=======================================================================================================================

        //==================================================================================================
        //Setting btn and message btn program

        mainBinding.settingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingPage.class);
                startActivity(intent);

            }
        });

    }
}