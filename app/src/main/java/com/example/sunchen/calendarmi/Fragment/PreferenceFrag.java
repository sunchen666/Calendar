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

        preGoal =  (String)this.getArguments().get("goal");
        View view = inflater.inflate(R.layout.preference, container, false);

        View save = view.findViewById(R.id.save_button);
        EditText edit = (EditText) view.findViewById(R.id.edit_goal);
//        save.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                if (preGoal.length() == 0) {
//                    ((MainActivity)getActivity()).saveNewGoal(edit.getText().toString());
//                } else {
//                    ((MainActivity)getActivity()).changeOldGoal(preGoal, edit.getText().toString());
//                }
//
//            }
//        });

        return view;
    }
}
