package com.example.sunchen.calendarmi.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.Activity.LoginActivity.Operation;
import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;
import com.sirvar.robin.ForgotPasswordFragment;
import com.sirvar.robin.RobinActivity;
import com.sirvar.robin.SignupFragment;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/*
 * This is fragment for Login page
 * Refer from Robin Library
 * */
public class LoginFrag extends Fragment {
    private TextView title;
    private TextView signup_link;
    private TextView forgot_pw;

    private ImageView image;
    private Button signin;
    private ImageButton google_signin;

    private EditText email;
    private EditText password;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    private SignupFragment signupFragment;
    private ForgotPasswordFragment forgotPasswordFragment;

    public LoginFrag() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        title = view.findViewById(R.id.title_login);
        signup_link = view.findViewById(R.id.signup_login);
        forgot_pw = view.findViewById(R.id.forgotPassword_login);

        image = view.findViewById(R.id.logo_login);
        signin = view.findViewById(R.id.submit_login);
        google_signin = view.findViewById(R.id.google_login);

        email = view.findViewById(R.id.email_login);
        password = view.findViewById(R.id.password_login);

        signupFragment = new SignupFragment();
        forgotPasswordFragment = new ForgotPasswordFragment();

        signin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try {
                    ((LoginActivity)getActivity()).loginOrSignup("", email.getText().toString(), password.getText().toString(), Operation.LOGIN);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        google_signin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                ((LoginActivity)getActivity()).googleLoginToApp();
            }
        });

        forgot_pw.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).startForPWFrag();
            }
        });

        signup_link.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).startSignupFrag();
            }
        });

        setAll();
        return view;
    }

    public void setImage(Drawable drawable) {
        logo_Drawable = drawable;
    }

    public void setImage(Bitmap bitmap) {
        logo_Bitmap = bitmap;
    }

    public void setImage() {
        if (logo_Drawable != null) {
            image.setImageDrawable(logo_Drawable);
        } else if (logo_Bitmap != null) {
            image.setImageBitmap(logo_Bitmap);
        }
    }

    private void setAll() {
        setImage();
    }
}
