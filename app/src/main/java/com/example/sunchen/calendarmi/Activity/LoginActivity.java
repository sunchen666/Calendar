package com.example.sunchen.calendarmi.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.sunchen.calendarmi.Fragment.ForgotPwFrag;
import com.example.sunchen.calendarmi.Fragment.LoginFrag;
import com.example.sunchen.calendarmi.Fragment.SignupFrag;
import com.example.sunchen.calendarmi.R;
import com.sirvar.robin.ForgotPasswordFragment;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.SignupFragment;
import com.sirvar.robin.Theme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/*
 * This is Activity for on Boarding Activities(Including sign in, sign up and forget password)
 * Refer from Robin Library
 * */

public class LoginActivity extends AppCompatActivity {
    private LoginFrag loginFragment;
    private SignupFrag signupFragment;
    private ForgotPwFrag forgotPasswordFragment;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    private boolean login_First = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robin);

        getSupportActionBar().hide();

        loginFragment = new LoginFrag();
        signupFragment = new SignupFrag();
        forgotPasswordFragment = new ForgotPwFrag();

        setImage(getResources().getDrawable(R.mipmap.ic_default_smile_logo_round, null));
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

    public void loginToApp(String email, String password) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void signupToApp(String name, String email, String password) {

    }

    public void sendVerification (String email) {

    }

    public void googleLoginToApp() {

    }

}
