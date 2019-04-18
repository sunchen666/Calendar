package com.example.sunchen.calendarmi.PreferenceSteps;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.sunchen.calendarmi.R;

import ernestoyaquello.com.verticalstepperform.Step;

public class FrequencyStep extends Step<String> {
    private View fresStepContent;
    private RadioButton rb_daily;
    private RadioButton rb_biweekly;
    private RadioButton rb_yearly;
    private RadioButton rb_monthly;
    private RadioButton rb_weekly;
    private RadioGroup rg;
    private String data_fre;
    private EditText untilET;

    public FrequencyStep(String title) {
        this(title, "");
    }

    public FrequencyStep(String title, String subtitle) {
        super(title, subtitle);
        data_fre = "";
    }

    @Override
    public String getStepData() {
        if (data_fre.equals("") || untilET.getText().equals("")) {
            return "";
        }

        return data_fre + " ; " + untilET.getText().toString();
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
        String[] datas = data.split(";");
        if (datas[0].contains("Weekly")) {
            rb_weekly.setChecked(true);
        } else if(datas[0].contains("Monthly")) {
            rb_monthly.setChecked(true);
        } else if(datas[0].contains("Daily")) {
            rb_daily.setChecked(true);
        } else if (datas[0].contains("Bi-Weekly")) {
            rb_biweekly.setChecked(true);
        } else if (datas[0].contains("Yearly")) {
            rb_yearly.setChecked(true);
        }

        if (untilET != null) {
            untilET.setText(datas[1]);
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
        fresStepContent = inflater.inflate(R.layout.frequency_layout, null, false);
        rg = fresStepContent.findViewById(R.id.frequency_step_rg);
        rb_daily = fresStepContent.findViewById(R.id.frequency_step_daily);
        rb_monthly = fresStepContent.findViewById(R.id.frequency_step_monthly);
        rb_biweekly = fresStepContent.findViewById(R.id.frequency_step_biweekly);
        rb_yearly = fresStepContent.findViewById(R.id.frequency_step_yearly);
        rb_weekly = fresStepContent.findViewById(R.id.frequency_step_weekly);
        untilET = fresStepContent.findViewById(R.id.frequency_step_until);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb_temp=fresStepContent.findViewById(checkedId);
                data_fre = rb_temp.getText().toString();
            }
        });

        return fresStepContent;
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
