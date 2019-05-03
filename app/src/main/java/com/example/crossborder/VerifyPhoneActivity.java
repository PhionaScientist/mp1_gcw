package com.example.crossborder;

import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.app.ProgressDialog.show;

public class VerifyPhoneActivity extends AppCompatActivity {

    EditText CodeField;
    Button btn_sign;
   // String CodeSent;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        CodeField=findViewById(R.id.CodeField);
        btn_sign = findViewById(R.id.buttonSign);
        mAuth = FirebaseAuth.getInstance();


        VerifyPhoneActivity phone = new VerifyPhoneActivity();


        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                verify();
            }
        });
    }

    private void verify()
    {
        String code = CodeField.getText().toString().trim();


        /// ahaa niho bihagarariye

        Intent intent = getIntent();
        String CodeSent = intent.getStringExtra("code");

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
                            Intent intent = new Intent(VerifyPhoneActivity.this,MainActivity.class);
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
}
