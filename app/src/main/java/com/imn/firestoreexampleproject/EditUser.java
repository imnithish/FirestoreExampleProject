package com.imn.firestoreexampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUser extends AppCompatActivity {

    EditText editFirstName;
    EditText editLastName;
    Button update;
    Button delete;

    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    private String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        editFirstName = findViewById(R.id.editFname);
        editLastName = findViewById(R.id.editLname);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("User");

        Intent intentt = getIntent();

        key = intentt.getStringExtra("keyy");
        Toast.makeText(this, key, Toast.LENGTH_SHORT).show();
        editFirstName.setText(intentt.getStringExtra("firstt"));
        editLastName.setText(intentt.getStringExtra("lastt"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateUser(key, editFirstName.getText().toString(), editLastName.getText().toString());

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteUser(key);
            }
        });
    }

    private void deleteUser(String key) {

        mDatabase.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditUser.this, "deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditUser.this, MyUsers.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUser.this, "something is  wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUser(String key, String toString, String toString1) {
        User user = new User(toString, toString1, mAuth.getUid(), key);
        mDatabase.child(key).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditUser.this, "updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditUser.this, MyUsers.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditUser.this, "something is  wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
