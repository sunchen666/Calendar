package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;
import com.example.sunchen.calendarmi.R;

import androidx.preference.PreferenceFragmentCompat;

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
