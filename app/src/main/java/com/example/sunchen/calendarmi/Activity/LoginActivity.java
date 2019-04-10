package com.example.sunchen.calendarmi.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Fragment.ForgotPwFrag;
import com.example.sunchen.calendarmi.Fragment.LoginFrag;
import com.example.sunchen.calendarmi.Fragment.SignupFrag;
import com.example.sunchen.calendarmi.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sirvar.robin.ForgotPasswordFragment;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.SignupFragment;
import com.sirvar.robin.Theme;
import com.victor.loading.rotate.RotateLoading;

import java.io.Console;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.gms.auth.api.signin.*;
import static android.provider.AlarmClock.EXTRA_MESSAGE;

/*
 * This is Activity for on Boarding Activities(Including sign in, sign up and forget password)
 * Refer from Robin Library
 * */

public class LoginActivity extends AppCompatActivity {
    public enum Operation {
        LOGIN,
        SIGNUP
    }

    private LoginFrag loginFragment;
    private SignupFrag signupFragment;
    private ForgotPwFrag forgotPasswordFragment;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    private RotateLoading rloading;
    OkHttpClient client = new OkHttpClient();

    private boolean login_First = true;

    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robin);

        getSupportActionBar().hide();

        loginFragment = new LoginFrag();
        signupFragment = new SignupFrag();
        forgotPasswordFragment = new ForgotPwFrag();

        setImage(getResources().getDrawable(R.drawable.birdlogo, null));
        setAll();
    }

    private void setAll() {
        showLoginFirst();
    }

    public void showLoginFirst() {
        login_First = true;
        if (findViewById(R.id.fragment_container) != null) {
            startLoginFrag();
        }
    }

    public void startLoginFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, loginFragment)
                .commit();
    }

    public void startSignupFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, signupFragment)
                .commit();
    }

    public void startForPWFrag() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, forgotPasswordFragment)
                .commit();
    }

    public void setImage(Drawable drawable) {
        logo_Drawable = drawable;
        loginFragment.setImage(logo_Drawable);
        signupFragment.setImage(logo_Drawable);
        forgotPasswordFragment.setImage(logo_Drawable);
    }

    public void setImage(Bitmap bitmap) {
        logo_Bitmap = bitmap;
        loginFragment.setImage(logo_Bitmap);
        signupFragment.setImage(logo_Bitmap);
        forgotPasswordFragment.setImage(logo_Bitmap);
    }

    public void loginOrSignup(String name, String email, String password, final Operation operation) throws ExecutionException, InterruptedException {
//        rloading.start();
        final String tempName = name;
        final String tempEmail = email;
        final String tempPassword = password;

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("name", tempName);
                builder.add("email", tempEmail);
                builder.add("password", tempPassword);

                String responseResult = "lalala";
                try {
                    int url = (operation == Operation.LOGIN) ? R.string.signin_server_link : R.string.signup_server_link;
                    responseResult = post(getString(url), builder.build());
                    System.out.println("responseResult: "+responseResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return responseResult;
            }

            @Override
            protected void onPostExecute(String s) {
                System.out.println("s: "+s);
                super.onPostExecute(s);
//                rloading.stop();
                try {
                    String response = s;
                    makeToast(this.get(), operation);
//                } catch (ExecutionException | InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        atask.execute();
    }

    public void makeToast(String response, Operation operation) {
        if (operation == Operation.LOGIN) {
            if (response.contains("Incorrect password!")){
                Toast.makeText(this, "Incorrect password!", Toast.LENGTH_LONG).show();
            } else if (response.contains("Email doesn't exist!")) {
                Toast.makeText(this, "Email doesn't exist!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Log in successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        } else if (operation == Operation.SIGNUP) {
            if (response.contains("Successfully")) {
                Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, OnBoardingActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to sign up", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        System.out.println("before newCall");
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            System.out.println("post response: "+res);
            return res;
        }
    }

    public void sendVerification (String email) {

    }

    public void googleLoginToApp() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    // [START config_signin]
    // Configure Google Sign In
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
    // [END config_signin]

    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    // [START initialize_auth]
    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();
    // [END initialize_auth]
}

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
}


