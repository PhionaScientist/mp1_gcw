package com.example.crossborder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import static com.example.crossborder.R.id.btn_facebook;
import static com.facebook.share.widget.MessageDialog.show;

public class Login extends AppCompatActivity {


    private Button Login;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    LoginButton btn_facebook;
    CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;







        @Override
             protected void onCreate(Bundle savedInstanceState)

                    {


                            super.onCreate(savedInstanceState);
                            setContentView(R.layout.activity_login);

                        firebaseAuth = FirebaseAuth.getInstance();

                        mAuthListener = new FirebaseAuth.AuthStateListener(){
                            @Override
                            public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user!=null){
                                    Intent intent = new Intent(Login.this, MapsActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }


                        };

                            FacebookSdk.sdkInitialize(getApplicationContext());
                            AppEventsLogger.activateApp(this);

                            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();



                            mGoogleSignInClient = GoogleSignIn.getClient(this,gso);


                            Login = findViewById(R.id.Login);
                            SignInButton btn_gmail = (SignInButton) findViewById(R.id.btn_gmail);
                            //btn_facebook = findViewById(R.id.btn_facebook);
                            mAuth = FirebaseAuth.getInstance();

                            callbackManager = CallbackManager.Factory.create();
                            btn_facebook = findViewById(R.id.btn_facebook);
                            btn_facebook.setReadPermissions("email", "public_profile");
                            btn_facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()

                                    {
                                @Override
                                    public void onSuccess(LoginResult loginResult)
                                        {
                                            handledFacebookAccessToken(loginResult.getAccessToken());

                                            Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Login.this,MainActivity.class);
                                            startActivity(intent);

                                        }

                                @Override
                                    public void onCancel()
                                        {

                                        }

                                @Override
                                    public void onError(FacebookException error)
                                        {

                                        }
                                    });




                            Login.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent intent = new Intent(Login.this,LoginWithNumber.class);
                                    startActivity(intent);
                                }

                            });

                            btn_gmail.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    signIn();
                                }
                            });
                             }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    private void signIn()
                            {
                                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                                startActivityForResult(signInIntent, 101);
                            }


                            @Override
                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                super.onActivityResult(requestCode, resultCode, data);

                                // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                                if (requestCode ==101) {
                                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                                    try {
                                        // Google Sign In was successful, authenticate with Firebase
                                        GoogleSignInAccount account = task.getResult(ApiException.class);
                                        firebaseAuthWithGoogle(account);

                                        Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Login.this,MainActivity.class);
                                        startActivity(intent);
                                    } catch (ApiException e) {
                                        // Google Sign In failed, update UI appropriately

                                        // ...
                                    }
                                }
                            }

                            private void handledFacebookAccessToken(AccessToken token)
                            {
                                AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
                                mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if (!task.isSuccessful())
                                        {
                                            Toast.makeText(Login.this,"Failed Login",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });


                                AuthCredential acredential = FacebookAuthProvider.getCredential(token.getToken());
                                mAuth.signInWithCredential(acredential)
                                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task)
                                            {
                                                if (task.isSuccessful()) {
                                                    // Sign in success, update UI with the signed-in user's information

                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                                    startActivity(intent);

                                                } else {
                                                    // If sign in fails, display a message to the user.

                                                    Toast.makeText(Login.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                                           }

                                                // ...
                                            }
                                        });
                            }

                            private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
                            {

                                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                                mAuth.signInWithCredential(credential)
                                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    // Sign in success, update UI with the signed-in user's information

                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    Toast.makeText(getApplicationContext(),"login succesfull",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Login.this,MainActivity.class);
                                                    startActivity(intent);



                                                }
                                                else
                                                    {
                                                    // If sign in fails, display a message to the user.

                                                    }

                                                // ...
                                            }
                                        });
                            }
}
