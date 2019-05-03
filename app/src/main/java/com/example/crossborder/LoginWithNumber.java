package com.example.crossborder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginWithNumber extends AppCompatActivity
{

    EditText PhoneField;
    EditText CodeField;
    Button Continue;
    String CodeSent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_number);


        PhoneField = findViewById(R.id.PhoneField);
        CodeField = findViewById(R.id.CodeField);
        Continue = findViewById(R.id.Continue);
        mAuth = FirebaseAuth.getInstance();




        Continue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                sendVerificationCode();




            }
        });

    }


    private void verify()
    {
        String code = CodeField.getText().toString().trim();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CodeSent, code);
        signInWithPhoneAuthCredential(credential);

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(LoginWithNumber.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            // ...
                        }
                        else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                Toast.makeText(getApplicationContext(),"login not succesfull",Toast.LENGTH_LONG).show();
                            }


                        }
                    }
                });
    }

    private void sendVerificationCode()
    {
        String phone = PhoneField.getText().toString();

        if(phone.isEmpty())
        {
            PhoneField.setError("Phone required to login");
            PhoneField.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
        {


        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {

                super.onCodeSent(s, forceResendingToken);
                CodeSent = s;
                Intent intent = new Intent(LoginWithNumber.this,VerifyPhoneActivity.class);
                intent.putExtra("code",CodeSent);
                startActivity(intent);


        }
    };



}
