package com.example.sunchen.calendarmi.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.Others.DescriptionPopUpWIndow;
import com.example.sunchen.calendarmi.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.SwitchPreference;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SettingFrag extends PreferenceFragmentCompat {
    private static final int REQUEST_NOTIFICATION_PERMISSION = 0x101;
    private static final int REQUEST_LOCATION_PERMISSION = 0x102;
    OkHttpClient client = new OkHttpClient();

    SwitchPreference reminder;
    SwitchPreference location;
    EditTextPreference set_home;
    EditTextPreference set_school;
    EditTextPreference set_working_place;
    Preference signout;

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_setting);

        reminder = (SwitchPreference) findPreference("switch1");
        location = (SwitchPreference) findPreference("switch3");
        set_home = (EditTextPreference) findPreference("switch_home") ;
        set_school = (EditTextPreference) findPreference("switch_school") ;
        set_working_place = (EditTextPreference) findPreference("switch_working_place") ;
        signout = findPreference("switch_log_out");

//
        reminder.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().contains("true")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, REQUEST_NOTIFICATION_PERMISSION);
                    }
                    reminder.setSwitchTextOn("true");
                } else {
                    Toast.makeText(getContext(), "Please go to default setting page to turn off the permission", Toast.LENGTH_LONG).show();
                    reminder.setSwitchTextOff("false");
                }
                return true;
            }
        });


        location.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (newValue.toString().contains("true")) {
                    Log.e("Test", "000");
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        Log.e("Test", "11");
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                    }
                    location.setSwitchTextOn("true");
                } else {
                    Toast.makeText(getContext(), "Please go to default setting page to turn off the permission", Toast.LENGTH_LONG).show();
                    location.setSwitchTextOff("false");
                }
                return true;
            }
        });

        set_home.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {
                final User currentUser = new User();
                final String place_val = newValue.toString();
                @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("place", place_val);
                        builder.add("type", "home");
                        builder.add("email", currentUser.getCurrentUser().getEmail());

                        String responseResult = "";
                        try {
                            int url = R.string.set_place_server_link;
                            String link = getContext().getString(url);
                            responseResult = post(link, builder.build());
                            System.out.println("responseResult: "+responseResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return responseResult;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Log.e("CHecked for answer:", s);
                        if (s.contains("Successfully")) {
                            //Add the response result
                        } else {
                            Toast.makeText(getContext(), "Set Home Fails", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                atask.execute();

                return true;
            }
        });

        set_school.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final User currentUser = new User();
                final String place_val = newValue.toString();
                @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("place", place_val);
                        builder.add("type", "school");
                        builder.add("email", currentUser.getCurrentUser().getEmail());

                        String responseResult = "";
                        try {
                            int url = R.string.set_place_server_link;
                            String link = getContext().getString(url);
                            responseResult = post(link, builder.build());
                            System.out.println("responseResult: "+responseResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return responseResult;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Log.e("CHecked for answer:", s);
                        if (s.contains("Successfully")) {
                            //Add the response result
                        } else {
                            Toast.makeText(getContext(), "Set Home Fails", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                atask.execute();
                return true;
            }
        });

        set_working_place.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                final User currentUser = new User();
                final String place_val = newValue.toString();
                @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>(){
                    @Override
                    protected String doInBackground(String... strings) {
                        FormBody.Builder builder = new FormBody.Builder();
                        builder.add("place", place_val);
                        builder.add("type", "working place");
                        builder.add("email", currentUser.getCurrentUser().getEmail());

                        String responseResult = "";
                        try {
                            int url = R.string.set_place_server_link;
                            String link = getContext().getString(url);
                            responseResult = post(link, builder.build());
                            System.out.println("responseResult: "+responseResult);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return responseResult;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Log.e("CHecked for answer:", s);
                        if (s.contains("Successfully")) {
                            //Add the response result
                        } else {
                            Toast.makeText(getContext(), "Set Home Fails", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                atask.execute();
                return true;
            }
        });

        signout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                User currentUser = new User();
                final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                Log.e("Auth", mAuth.getCurrentUser().getEmail());

                if (mAuth.getCurrentUser().getEmail().contains("@")) {
                    GoogleSignInClient mGoogleSignInClient = User.getCurrentUser().getmGoogleSignInClient();

                    mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Log.e("Testing", "Try to do it");
                            mAuth.signOut();
                        }
                    });
                }

                currentUser.setCurrentUser(new User("", "", ""));
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();

                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Reminder Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Go to the setting system in the phone", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Go to the setting system in the phone", Toast.LENGTH_SHORT).show();
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

}
