package com.example.sunchen.calendarmi.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
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
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private Activity currentActivity;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    GoogleSignInAccount signInAccount;

    private User currentUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robin);

        getSupportActionBar().hide();

        loginFragment = new LoginFrag();
        signupFragment = new SignupFrag();
        forgotPasswordFragment = new ForgotPwFrag();

        currentActivity = this;

        setImage(getResources().getDrawable(R.drawable.birdlogo, null));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.oauth_request_id_google))
                .requestIdToken(getString(R.string.oauth_request_id_google))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
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
                    User user = new User(tempName, tempEmail, tempPassword);
                    makeToast(this.get(), operation, user);
//                } catch (ExecutionException | InterruptedException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        atask.execute();
    }

    public void makeToast(String response, Operation operation, User user) {
        if (operation == Operation.LOGIN) {
            if (response.contains("Incorrect password!")){
                Toast.makeText(this, "Incorrect password!", Toast.LENGTH_LONG).show();
            } else if (response.contains("Email doesn't exist!")) {
                Toast.makeText(this, "Email doesn't exist!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Log in successfully", Toast.LENGTH_LONG).show();
                user.setCurrentUser(user);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        } else if (operation == Operation.SIGNUP) {
            if (response.contains("Successfully")) {
                Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show();
                user.setCurrentUser(user);
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
        startActivityForResult(signInIntent, 9001);
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                System.out.println("ApiException");
                e.printStackTrace();
                Toast.makeText(this, "Google auth failed!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = mAuth.getUid();
                            getGoogleUidFromFirebase(uid);
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.login), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    // [END auth_with_google]
    private void getGoogleUidFromFirebase(final String uid) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        final DatabaseReference usersRef = db.getReference("users");
        Query query = usersRef.orderByKey().equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    usersRef.child(uid);
                    User u = new User();
                    usersRef.setValue(u);
                    Intent intent = new Intent(currentActivity, OnBoardingActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(currentActivity, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


