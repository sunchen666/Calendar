package com.example.sunchen.calendarmi.PreferenceSteps;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import ernestoyaquello.com.verticalstepperform.Step;

public class ScheduleStep extends Step<boolean[]>{
    private boolean[] days;
    private View daysStepContent;

    public ScheduleStep(String title) {
        this(title, "");
    }

    public ScheduleStep(String title, String subtitle) {
        super(title, subtitle);
    }

    @Override
    public boolean[] getStepData() {
        return days;
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        String[] weekDayStrings = getContext().getResources().getStringArray(R.array.week_days_extended);
        List<String> selectedWeekDayStrings = new ArrayList<>();
        for (int i = 0; i < weekDayStrings.length; i++) {
            if (days[i]) {
                selectedWeekDayStrings.add(weekDayStrings[i]);
            }
        }

        return TextUtils.join(", ", selectedWeekDayStrings);

    }

    @Override
    public void restoreStepData(boolean[] data) {
        days = data;
        setupAlarmDays();
    }

    @Override
    protected IsDataValid isStepDataValid(boolean[] stepData) {
        boolean thereIsAtLeastOneDaySelected = false;
        for(int i = 0; i < stepData.length && !thereIsAtLeastOneDaySelected; i++) {
            if(stepData[i]) {
                thereIsAtLeastOneDaySelected = true;
            }
        }

        return thereIsAtLeastOneDaySelected
                ? new IsDataValid(true)
                : new IsDataValid(false, "Select at least one day");

    }

    @Override
    protected View createStepContentLayout() {
        // We create this step view by inflating an XML layout
        LayoutInflater inflater = LayoutInflater.from(getContext());
        daysStepContent = inflater.inflate(R.layout.step_days_of_week_layout, null, false);
        setupAlarmDays();

        return daysStepContent;
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

    private void setupAlarmDays() {
        boolean firstSetup = days == null;
        days = firstSetup ? new boolean[7] : days;

        final String[] weekDays = getContext().getResources().getStringArray(R.array.week_days);
        for(int i = 0; i < weekDays.length; i++) {
            final int index = i;
            final View dayLayout = getDayLayout(index);

            if (firstSetup) {
                // By default, we only mark the working days as activated
                days[index] = index < 5;
            }

            updateDayLayout(index, dayLayout, false);

            if (dayLayout != null) {
                dayLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        days[index] = !days[index];
                        updateDayLayout(index, dayLayout, true);
                        markAsCompletedOrUncompleted(true);
                    }
                });

                final TextView dayText = dayLayout.findViewById(R.id.day_preference);
                dayText.setText(weekDays[index]);
            }
        }
    }

    private View getDayLayout(int i) {
        int id = daysStepContent.getResources().getIdentifier(
                "day_" + i + "_preference", "id", getContext().getPackageName());
        return daysStepContent.findViewById(id);
    }

    private void updateDayLayout(int dayIndex, View dayLayout, boolean useAnimations) {
        if (days[dayIndex]) {
            markAlarmDay(dayIndex, dayLayout, useAnimations);
        } else {
            unmarkAlarmDay(dayIndex, dayLayout, useAnimations);
        }
    }

    private void markAlarmDay(int dayIndex, View dayLayout, boolean useAnimations) {
        days[dayIndex] = true;

        if (dayLayout != null) {
            Drawable bg = ContextCompat.getDrawable(getContext(), ernestoyaquello.com.verticalstepperform.R.drawable.circle_step_done);
            int colorPrimary = ContextCompat.getColor(getContext(), R.color.colorFireBrick);
            bg.setColorFilter(new PorterDuffColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN));
            dayLayout.setBackground(bg);

            TextView dayText = dayLayout.findViewById(R.id.day_preference);
            dayText.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void unmarkAlarmDay(int dayIndex, View dayLayout, boolean useAnimations) {
        days[dayIndex] = false;

        dayLayout.setBackgroundResource(0);

        TextView dayText = dayLayout.findViewById(R.id.day_preference);
        int colour = ContextCompat.getColor(getContext(), R.color.colorFireBrick);
        dayText.setTextColor(colour);
    }



}
