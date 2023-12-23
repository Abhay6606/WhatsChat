package com.example.whatschat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.whatschat.databinding.ActivityLoginBinding;
import com.example.whatschat.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding mainBinding;
    Uri imageUri;
    String imageuri;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    String status = "User is now Active";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Establishing the Account");
        progressDialog.setCancelable(false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        mainBinding.rgLoginID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mainBinding.rgSignupId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mainBinding.rgNameId.getText().toString();
                String email = mainBinding.rgEmailId.getText().toString();
                String password = mainBinding.rgPassword.getText().toString();
                String rePassword = mainBinding.rgRePassword.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(rePassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Provide appropriate details", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    mainBinding.rgRePassword.setError("Provide appropriate email address");
                } else if (password.length() < 7) {
                    progressDialog.dismiss();
                    mainBinding.rgPassword.setError("Password length shoud be more then six charecter");
                } else if (!password.equals(rePassword)) {
                    progressDialog.dismiss();
                    mainBinding.rgPassword.setError("Password does not match");
                } else {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("User").child(id);
                                StorageReference storageReference = storage.getReference().child("upload").child(id);

                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri = uri.toString();
                                                        UsserGettersetter pack = new UsserGettersetter(name, email, password,id, imageuri, status);
                                                        reference.setValue(pack).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isComplete()) {
                                                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else {
                                                                    Toast.makeText(RegisterActivity.this, "error in creating account ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {

                          //          imageuri = " ";
                                    imageuri = "https://firebasestorage.googleapis.com/v0/b/whatschat-fe022.appspot.com/o/friend.png?alt=media&token=ae0a3b59-fc8b-4fa7-a892-6851e03e70b4";
                                    UsserGettersetter usserGettersetter = new UsserGettersetter(name, email, password, id, imageuri, status);
                                    reference.setValue(usserGettersetter).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()) {
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                Toast.makeText(RegisterActivity.this, "error in creating account !", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                }

                            } else {

                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });

        mainBinding.usserImageId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//for all type of image jpg,png.....
                intent.setAction(Intent.ACTION_GET_CONTENT);//explicite intent for
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 06);

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 06) {
            if (data != null) {
                imageUri = data.getData();
                mainBinding.usserImageId.setImageURI(imageUri);
            }
        }
    }
}