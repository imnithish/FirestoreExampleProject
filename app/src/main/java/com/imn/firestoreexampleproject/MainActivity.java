package com.imn.firestoreexampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

    FirebaseAuth mAuth;

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
        loadData.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();


        mDatabase = FirebaseDatabase.getInstance().getReference("User");

        saveAndPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mDatabase.push().getKey();
                writeNewUser(id, first.getText().toString(), second.getText().toString());
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList = new ArrayList<User>();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    Log.e("userDetailsv2", "" + dataSnapshot2.getValue());
                    if (dataSnapshot2.getValue() instanceof User) {
                        User user = dataSnapshot2.getValue(User.class);
                        userList.add(user);
                        Log.e("userDetails", user.getFirstName() + "  " + user.getLastName());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            User user = dataSnapshot2.getValue(User.class);
                            if(user.getUserID().equals(mAuth.getUid()))
                            userList.add(user);

                        }
                        setAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
    }


    private void writeNewUser(String id, String firstt, String lastt) {

        User user = new User(firstt, lastt,mAuth.getUid());
        mDatabase.child(id).setValue(user);


    }

    private void setAdapter() {
        Log.e("Details", "" + userList.size());
        loadData.setAdapter(new UserAdapter(userList));

    }
}
