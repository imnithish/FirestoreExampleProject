package com.imnstudios.firestoreexampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ArrayList<User> userList;
    RecyclerView loadData;

    EditText first;
    EditText second;
    Button saveAndPublish;
    Button show;
    Button myUsers;
    Button logOut;
    private String key;
    FirebaseAuth mAuth;

    Button selectImage;
    ImageView imagePreview;
    String imageUri;
    Uri selectedfile;

    FirebaseStorage storage;
    StorageReference storageReference;
    String imageUrl = "null";
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first = findViewById(R.id.firstNameEt);
        second = findViewById(R.id.lastNameEt);
        saveAndPublish = findViewById(R.id.continueBt);
        show = findViewById(R.id.authoruzedTV);
        loadData = findViewById(R.id.loadData);

        myUsers = findViewById(R.id.myusers);
        logOut = findViewById(R.id.logout);

        selectImage = findViewById(R.id.selectimage);
        imagePreview = findViewById(R.id.imagepreview);
        loadData.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        mDatabase = FirebaseDatabase.getInstance().getReference("User");


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("image/*")
                        .setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);

            }
        });


        saveAndPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


        final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList = new ArrayList<>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    User user = dataSnapshot2.getValue(User.class);
//                            if(user.getUserID().equals(mAuth.getUid()))
                    userList.add(user);

                }
                setAdapter();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Query query = FirebaseDatabase.getInstance().getReference("User")
                        .orderByChild("lastName");
                query.addListenerForSingleValueEvent(valueEventListener);


            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.getInstance().signOut();
                SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs2", Context.MODE_PRIVATE);
                pref.edit().clear().commit();
                startActivity(new Intent(MainActivity.this, GetInActivity.class));

            }
        });

        myUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyUsers.class));
            }
        });
    }

    private void uploadImage() {
        if (selectedfile != null) {
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());
            uploadTask = ref.putFile(selectedfile);

            // adding listeners on upload
            // or failure of image
            uploadTask.addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(
                                UploadTask.TaskSnapshot taskSnapshot) {

                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast
                                    .makeText(MainActivity.this,
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                    .show();


//                                    Log.i("imageurl","imageurl: "+imageUrl);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    // Error, Image not uploaded
                    progressDialog.dismiss();
                    Toast
                            .makeText(MainActivity.this,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                }
            }).addOnProgressListener(
                    new OnProgressListener<UploadTask.TaskSnapshot>() {

                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot) {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (int) progress + "%");
                        }
                    });

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageUrl = String.valueOf(downloadUri);
                        Log.i("image url", "url: " + imageUrl);

                        key = mDatabase.push().getKey();
                        writeNewUser(key, first.getText().toString(), second.getText().toString(), imageUrl);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });

        }
    }


    private void writeNewUser(String key, String firstt, String lastt, String imageUrl) {

        Log.i("image url on push", "url: " + imageUrl);
        User user = new User(firstt, lastt, mAuth.getUid(), key, imageUrl);
        mDatabase.child(key).setValue(user);


    }

    private void setAdapter() {
        Log.e("Details", "" + userList.size());
        loadData.setAdapter(new UserAdapter(getApplicationContext(), userList));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            selectedfile = data.getData(); //The uri with the location of the file
            imageUri = selectedfile.toString();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedfile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imagePreview.setImageDrawable(roundedBitmapDrawable);

        }
    }
}
