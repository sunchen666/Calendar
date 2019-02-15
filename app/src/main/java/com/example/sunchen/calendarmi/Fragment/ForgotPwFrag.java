package com.example.sunchen.calendarmi.Fragment;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.R;

/*
* This is fragment for forgot password page
* Refer from Robin Library
* */
public class ForgotPwFrag extends Fragment {
    private TextView title;
    private TextView login;
    private ImageView image;
    private EditText email;
    private Button submit;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    public ForgotPwFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        // Initialize views
        title =  view.findViewById(R.id.title);
        login =  view.findViewById(R.id.login);
        image =  view.findViewById(R.id.logo);
        email =  view.findViewById(R.id.email);
        submit = view.findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).sendVerification(email.getText().toString());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).startLoginFrag();
            }
        });

        setAll();
        return view;
    }

    public void setImage (Drawable drawable) {
       logo_Drawable = drawable;
    }

    public void setImage (Bitmap bitmap) {
        logo_Bitmap = bitmap;
    }

    public void setImage() {
        if (logo_Drawable != null) {
            image.setImageDrawable(logo_Drawable);
        }  else if(logo_Bitmap != null) {
            image.setImageBitmap(logo_Bitmap);
        }
    }


    private void setAll() {
        setImage();
    }
}
