package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Activity.LoginActivity;
import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;

public class SettingFrag extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.fragment_setting);
//
//        calendar_pre = findPreference("key_calendar");
//
//        calendar_pre.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                ((MainActivity)getActivity()).startCalendarFrag();
//                return true;
//            }
//        });
    }
}
