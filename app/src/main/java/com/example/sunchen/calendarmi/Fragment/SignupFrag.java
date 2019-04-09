package com.example.sunchen.calendarmi.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.Activity.LoginActivity.Operation;
import com.example.sunchen.calendarmi.Activity.OnBoardingActivity;
import com.example.sunchen.calendarmi.R;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private RotateLoading rloading;

    private Drawable logo_Drawable;
    private Bitmap logo_Bitmap;

    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();


    public SignupFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signingup, container, false);

        // Initialize views
        title = view.findViewById(R.id.title_signup);
        login = view.findViewById(R.id.login_signup);
        image = view.findViewById(R.id.logo_signup);
        name = view.findViewById(R.id.name_signup);
        email = view.findViewById(R.id.email_signup);
        password = view.findViewById(R.id.password_signup);
        confirmPassword = view.findViewById(R.id.confirm_password_signup);
        submit = view.findViewById(R.id.submit_signup);
        rloading = view.findViewById(R.id.rotateloading_signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).startLoginFrag();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ((LoginActivity)getActivity()).loginOrSignup(name.getText().toString(), email.getText().toString(), password.getText().toString(), Operation.SIGNUP);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
