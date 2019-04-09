package com.example.sunchen.calendarmi.Fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.R;
import android.widget.Button;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.victor.loading.rotate.RotateLoading;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarFrag extends Fragment {

    private Button googleButton;
    private RotateLoading rloading;
    private boolean isSyncCalendar = false;
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        googleButton = view.findViewById(R.id.google_calendar_sync);
        rloading = view.findViewById(R.id.rotateloading_calendar);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user doesn't have google user's token, authenticate with google account
                isSyncCalendar = true;
                ((MainActivity)getActivity()).syncGoogleCalendarSignin();
                //if user has google user's token
            }
        });
        return view;
    }

    public void sendAuthInfo(final String authCode) throws ExecutionException, InterruptedException {
        rloading.start();

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("auth", authCode);
                builder.add("id", getString(R.string.oauth_request_id_google));
                builder.add("secret", getString(R.string.oauth_request_secret_google));

                Log.e("Send auth", authCode);
                Log.e("Send id", getString(R.string.oauth_request_id_google));
                Log.e("Send secret", getString(R.string.oauth_request_secret_google));

                String responseResult = "";
                try {
                    responseResult = post(getString(R.string.sync_calendar_server_link), builder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return responseResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                rloading.stop();
                isSyncCalendar = false;
            }
        };

        atask.execute();
        Log.e("Checkedcc", atask.get());
        //Sign up successfully
        if (atask.get().contains("Successfully")) {
            Toast.makeText(getActivity(), "Sync successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Failed to sync calendar", Toast.LENGTH_LONG).show();
        }
    }

    public String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public boolean getIsSyncCalendar() {
        return isSyncCalendar;
    }
}
