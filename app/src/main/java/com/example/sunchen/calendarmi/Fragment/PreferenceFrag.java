package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.Map;

public class PreferenceFrag extends Fragment {

    private String preGoal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preference, container, false);

        return view;
    }
}
