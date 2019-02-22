package com.example.sunchen.calendarmi.PreferenceSteps;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import ernestoyaquello.com.verticalstepperform.Step;

public class GoalDescriptionStep extends Step<String> {
    private TextInputEditText des_editText;

    public GoalDescriptionStep(String title) {
        this(title, "");
    }

    public GoalDescriptionStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @Override
    public String getStepData() {
        Editable text = des_editText.getText();
        if (text != null) {
            return text.toString();
        }

        return "";
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String description = getStepData();
        return description == null || description.isEmpty()
                ? "Empty"
                : description;
    }

    @Override
    public void restoreStepData(String data) {
        if (des_editText != null) {
            des_editText.setText(data);
        }
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        return new IsDataValid(true);
    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view programmatically
        des_editText = new TextInputEditText(getContext());
        des_editText.setHint("Please Input Description of Goal:");
        des_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getFormView().goToNextStep(true);
                return false;
            }
        });

        return des_editText;
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
