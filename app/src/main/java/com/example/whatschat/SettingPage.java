package com.example.whatschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatschat.databinding.ActivitySettingPageBinding;
import com.example.whatschat.databinding.DialogLayoutBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SettingPage extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    String editEmail, editPassword;
    Uri editImageUri;


    ActivitySettingPageBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivitySettingPageBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference().child("User").child(auth.getUid());
        StorageReference storageReference = firebaseStorage.getReference().child("upload").child(auth.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                editEmail = snapshot.child("email").getValue().toString();
                editPassword = snapshot.child("password").getValue().toString();
                mainBinding.settingName.setText(snapshot.child("name").getValue().toString());
                mainBinding.settingStatus.setText(snapshot.child("status").getValue().toString());
                Picasso.get().load(snapshot.child("imageuri").getValue().toString()).into(mainBinding.SettingImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mainBinding.SettingSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mainBinding.settingName.getText().toString();
                String status = mainBinding.settingStatus.getText().toString();
                if (editImageUri != null) {
                    storageReference.putFile(editImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri = uri.toString();
                                    UsserGettersetter pack = new UsserGettersetter(name, editEmail, editPassword, auth.getUid(), finalImageUri, status);
                                    reference.setValue(pack).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SettingPage.this, "Your details are updated.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(SettingPage.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(SettingPage.this, "Something went wrong.", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });

                                }
                            });
                        }
                    });
                } else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String finalImageUri = uri.toString();
                            UsserGettersetter pack = new UsserGettersetter(name, editEmail, editPassword, auth.getUid(), finalImageUri, status);
                            reference.setValue(pack).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SettingPage.this, "Your details are updated.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SettingPage.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(SettingPage.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });

        mainBinding.SettingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 06);
            }
        });



        //==============================================================================================
        mainBinding.logOutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(SettingPage.this, R.style.dialog);
                DialogLayoutBinding layoutBinding = DialogLayoutBinding.inflate(getLayoutInflater());
                dialog.setContentView(layoutBinding.getRoot());

                layoutBinding.yesBtnId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(SettingPage.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                layoutBinding.noBtnId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 06) {
            if (data != null) {
                editImageUri = data.getData();
                mainBinding.SettingImage.setImageURI(editImageUri);

            }
        }
    }







}