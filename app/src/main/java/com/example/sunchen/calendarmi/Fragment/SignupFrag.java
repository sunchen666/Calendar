package com.example.sunchen.calendarmi.Fragment;

import android.annotation.SuppressLint;
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
                    signup(name.getText().toString(), email.getText().toString(), password.getText().toString());
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

    public void signup(String name, String email, String password) throws ExecutionException, InterruptedException {
        rloading.start();
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

                String responseResult = "";
                try {
                    responseResult = post(getString(R.string.signup_server_link), builder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return responseResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                rloading.stop();
            }
        };

        atask.execute();
        Log.e("Checked", atask.get());
        //Sign up successfully
        if (atask.get().contains("Successfully")) {
            Toast.makeText(getActivity(), "Sign up successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Failed to sign up", Toast.LENGTH_LONG).show();
        }

    }

    String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
