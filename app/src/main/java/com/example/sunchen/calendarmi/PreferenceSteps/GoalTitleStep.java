package com.example.sunchen.calendarmi.PreferenceSteps;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.sunchen.calendarmi.R;
import com.google.android.material.textfield.TextInputEditText;

import ernestoyaquello.com.verticalstepperform.Step;

public class GoalTitleStep extends Step<String> {

    private static final int MIN_CHARACTERS_ALARM_NAME = 3;
    private TextInputEditText goal_edittext;
    private String unformattedErrorString;

    public GoalTitleStep(String goal) {
        this(goal, "");
    }

    public GoalTitleStep(String goal, String sub_goal) {
        super(goal, sub_goal);
    }

    @Override
    public String getStepData() {
        Editable text = goal_edittext.getText();
        if (text != null) {
            return text.toString();
        }

        return "";
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
        if (goal_edittext != null) {
            goal_edittext.setText(data);
        }
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        if (stepData.length() < MIN_CHARACTERS_ALARM_NAME) {
            String titleError = String.format(unformattedErrorString, MIN_CHARACTERS_ALARM_NAME);
            return new IsDataValid(false, titleError);
        } else {
            return new IsDataValid(true);
        }
    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view programmatically
        goal_edittext = new TextInputEditText(getContext());
        goal_edittext.setHint("Please Input the goal");
        goal_edittext.setSingleLine(true);
        goal_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                markAsCompletedOrUncompleted(true);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        goal_edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getFormView().goToNextStep(true);
                return false;
            }
        });

        unformattedErrorString = "At least" +  MIN_CHARACTERS_ALARM_NAME + " characters";

        return goal_edittext;
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
