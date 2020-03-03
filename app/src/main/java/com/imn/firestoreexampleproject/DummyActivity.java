package com.imn.firestoreexampleproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DummyActivity extends AppCompatActivity {

    private TextView firstName;
    private  TextView lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);

        setTitle("dummy");


        firstName = findViewById(R.id.fn);
        lastName = findViewById(R.id.ln);

        Intent intent = getIntent();
        firstName.setText(intent.getStringExtra("first"));
        lastName.setText(intent.getStringExtra("last"));



    }
}
