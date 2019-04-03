package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

public class CalendarFrag extends Fragment {
    private Button googleButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        googleButton = view.findViewById(R.id.google_calendar_sync);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if user doesn't have google user's token, authenticate with google account
                ((MainActivity)getActivity()).syncGoogleCalendarSignin();

                //if user has google user's token
            }
        });
        return view;
    }
}
