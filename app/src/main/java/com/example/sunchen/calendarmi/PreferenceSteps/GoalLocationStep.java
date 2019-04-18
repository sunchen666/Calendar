package com.example.sunchen.calendarmi.PreferenceSteps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sunchen.calendarmi.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class GoalLocationStep extends Step<String>{
    private View locationStepContent;
    private RadioButton rb_home;
    private RadioButton rb_school;
    private RadioButton rb_work;
    private RadioButton rb_every;

    private RadioGroup rg;
    private String data_loc;

    public GoalLocationStep(String title) {
        this(title, "");
    }

    public GoalLocationStep(String title, String subtitle) {
        super(title, subtitle);
        data_loc = "";
    }

    @Override
    public String getStepData() {
        if (data_loc.equals("")) {
            return "";
        }

        return data_loc;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String name = getStepData();
        return name == null || name.isEmpty()
                ? "Empty"
                : name;
    }

    @Override
    public void restoreStepData(String data) {
        if (data.equals("Home")) {
            rb_home.setChecked(true);
        } else if(data.equals("Working Place")) {
            rb_work.setChecked(true);
        } else if (data.equals("School")) {
            rb_school.setChecked(true);
        } else if (data.equals("Every places")) {
            rb_every.setChecked(true);
        }
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(true);
    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        locationStepContent = inflater.inflate(R.layout.step_location_preferences_layout, null, false);
        rg = locationStepContent.findViewById(R.id.location_step_rg);
        rb_home = locationStepContent.findViewById(R.id.location_step_home);
        rb_work = locationStepContent.findViewById(R.id.location_step_working);
        rb_school = locationStepContent.findViewById(R.id.location_step_school);
        rb_every = locationStepContent.findViewById(R.id.frequency_step_places);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb_temp=locationStepContent.findViewById(checkedId);
                data_loc = rb_temp.getText().toString();
            }
        });

        return locationStepContent;
    }

    @Override
    protected void onStepOpened(boolean animated) {

    }

    @Override
    protected void onStepClosed(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {

    }
}
