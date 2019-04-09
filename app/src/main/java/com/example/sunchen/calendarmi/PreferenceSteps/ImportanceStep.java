package com.example.sunchen.calendarmi.PreferenceSteps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sunchen.calendarmi.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class ImportanceStep extends Step<String> {
    private View locationStepContent;
    private RadioButton rb_low;
    private RadioButton rb_avg;
    private RadioButton rb_high;

    private RadioGroup rg;
    private String data_importance;

    public ImportanceStep(String title) {
        this(title, "");
    }

    public ImportanceStep(String title, String subtitle) {
        super(title, subtitle);
        data_importance = "";
    }

    @Override
    public String getStepData() {
        if (data_importance.equals("")) {
            return "";
        }

        return data_importance;
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
        if (data_importance.equals("Low")) {
            rb_low.setChecked(true);
        } else if(data_importance.equals("Avg.")) {
            rb_avg.setChecked(true);
        } else if (data_importance.equals("High")) {
            rb_high.setChecked(true);
        }
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return null;
    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        locationStepContent = inflater.inflate(R.layout.step_importance_preferences_layout, null, false);
        rg = locationStepContent.findViewById(R.id.importance_step_rg);
        rb_low = locationStepContent.findViewById(R.id.low_importance_step);
        rb_avg = locationStepContent.findViewById(R.id.avg_importance_step);
        rb_high = locationStepContent.findViewById(R.id.high_importance_step);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb_temp=locationStepContent.findViewById(checkedId);
                data_importance = rb_temp.getText().toString();
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
