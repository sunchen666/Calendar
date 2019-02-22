package com.example.sunchen.calendarmi.Fragment;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.R;

import androidx.fragment.app.Fragment;

/*
 * This is fragment for sign up page
 * Refer from Robin Library
 * */
public class SignupFrag extends Fragment {
    private TextView title;
    private TextView login;
    private ImageView image;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button submit;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    public SignupFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Initialize views
        title = view.findViewById(R.id.title);
        login = view.findViewById(R.id.login);
        image = view.findViewById(R.id.logo);
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        submit = view.findViewById(R.id.submit);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).startLoginFrag();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).signupToApp(name.getText().toString(), email.getText().toString(), password.getText().toString());

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
