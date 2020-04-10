package com.imnstudios.firestoreexampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class GetInActivity extends AppCompatActivity {

    private EditText Phone;
    private Button GetOtp;
    private EditText Otp;
    private Button GetIn;
    private String codeSent;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_in);

        if (restorePrefData()) {
            Intent mainActivity = new Intent(GetInActivity.this, MainActivity.class);
            startActivity(mainActivity);
            finish();

        }

        Phone = findViewById(R.id.editTextPhone);
        GetOtp = findViewById(R.id.buttonGetOtp);
        Otp = findViewById(R.id.editTextOtp);
        GetIn = findViewById(R.id.buttonGetIn);

        firebaseAuth = firebaseAuth.getInstance();

        GetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode();
            }
        });

        GetIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyGetInCode();

            }
        });
    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs2", MODE_PRIVATE);
        Boolean isIntroActivityOpnendBefore = pref.getBoolean("isIntroOpnendd", false);
        return isIntroActivityOpnendBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs2", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpnendd", true);
        editor.commit();
    }


    private void sendVerificationCode() {

        String phoneNumber = Phone.getText().toString();
        if (phoneNumber.isEmpty() || (phoneNumber.length() < 10 || (phoneNumber.length() > 10))) {
            Phone.setError("ten digit phone number is required.");
            Phone.requestFocus();
            return;
        }

        Otp.setVisibility(View.VISIBLE);
        GetIn.setVisibility(View.VISIBLE);
        GetOtp.setVisibility(View.INVISIBLE);


        String phoneNumber2 = "+91" + phoneNumber;

        Otp.setVisibility(View.VISIBLE);
        GetIn.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber2,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Toast.makeText(GetInActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(GetInActivity.this, "Quota Exceeded", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;

        }
    };

    private void verifyGetInCode() {
        String code = Otp.getText().toString();

        if (code.isEmpty()){
            Toast.makeText(this, "please enter otp", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //open activity and proceed
                            savePrefsData();
                            startActivity(new Intent(GetInActivity.this, MainActivity.class));


                        } else {
                            String message = "Somthing's wrong. Check Internet Connectivity!";

                            Toast toast = Toast.makeText(GetInActivity.this, message, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                Toast.makeText(GetInActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
