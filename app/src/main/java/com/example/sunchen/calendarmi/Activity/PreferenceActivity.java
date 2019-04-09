package com.example.sunchen.calendarmi.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import com.example.sunchen.calendarmi.PreferenceSteps.FrequencyStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalDescriptionStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalLocationStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalTitleStep;
import com.example.sunchen.calendarmi.PreferenceSteps.ImportanceStep;
import com.example.sunchen.calendarmi.PreferenceSteps.ScheduleStep;
import com.example.sunchen.calendarmi.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class PreferenceActivity extends AppCompatActivity implements StepperFormListener, DialogInterface.OnClickListener  {
    public static final String STATE_NEW_GOAL_ADDED = "new_goal_added";
    public static final String STATE_GOAL = "goal";
    public static final String STATE_DESCRIPTION = "description";
    public static final String STATE_WEEK_DAYS = "week_days";
    public static final String STATE_FREQUENCY = "frequency";
    public static final String STATE_LOCATION = "location";
    public static final String STATE_IMPORTANCE = "importance";

    private ProgressDialog progressDialog;
    private VerticalStepperFormView verticalStepperForm;

    private GoalTitleStep goalStep;
    private GoalDescriptionStep descriptionStep;
    private ScheduleStep scheduleStep;
    private FrequencyStep frequencyStep;
    private GoalLocationStep locationStep;
    private ImportanceStep importanceStep;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        String[] stepTitles = getResources().getStringArray(R.array.steps_titles);

        goalStep = new GoalTitleStep(stepTitles[0]);//, stepSubtitles[0]);
        descriptionStep = new GoalDescriptionStep(stepTitles[1]);//, stepSubtitles[1]);
        scheduleStep = new ScheduleStep(stepTitles[2]);//, stepSubtitles[2]);
        frequencyStep = new FrequencyStep(stepTitles[3]);
        locationStep = new GoalLocationStep(stepTitles[4]);
        importanceStep = new ImportanceStep(stepTitles[5]);

        verticalStepperForm = findViewById(R.id.stepper_form_preferences);
        verticalStepperForm.setup(this, goalStep, descriptionStep, scheduleStep, frequencyStep, locationStep, importanceStep).init();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {

            // "Discard" button of the Discard goal dialog
            case -1:
                finish();
                break;

            // "Cancel" button of the Discard goal dialog
            case -2:
                verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
                break;
        }
    }

    @Override
    public void onCompletedForm() {
        final Thread dataSavingThread = saveData();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setMessage("Adding new goal...");
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                try {
                    dataSavingThread.interrupt();
                } catch (RuntimeException e) {
                    // No need to do anything here
                } finally {
                    verticalStepperForm.cancelFormCompletionOrCancellationAttempt();
                }
            }
        });
    }

    @Override
    public void onCancelledForm() {
        showCloseConfirmationDialog();
    }

    private Thread saveData() {

        // Fake data saving effect
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    intent.putExtra(STATE_NEW_GOAL_ADDED, true);
                    intent.putExtra(STATE_GOAL, goalStep.getStepData());
                    intent.putExtra(STATE_DESCRIPTION, descriptionStep.getStepData());
                    intent.putExtra(STATE_WEEK_DAYS, scheduleStep.getStepData());
                    intent.putExtra(STATE_FREQUENCY, frequencyStep.getStepData());
                    intent.putExtra(STATE_LOCATION, locationStep.getStepData());
                    intent.putExtra(STATE_IMPORTANCE, importanceStep.getStepData());

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        return thread;
    }

    @Override
    public void onBackPressed() {
        finishIfPossible();
    }

    @Override
    protected void onPause() {
        super.onPause();

        dismissDialogIfNecessary();
    }

    @Override
    protected void onStop() {
        super.onStop();

        dismissDialogIfNecessary();

    }

    private void showCloseConfirmationDialog() {
        new DiscardAlarmConfirmationFragment().show(getSupportFragmentManager(), null);
    }

    private void finishIfPossible() {
        if(verticalStepperForm.isAnyStepCompleted()) {
            showCloseConfirmationDialog();
        } else {
            finish();
        }
    }

    private void dismissDialogIfNecessary() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finishIfPossible();
            return true;
        }

        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_GOAL, goalStep.getStepData());
        outState.putString(STATE_DESCRIPTION, descriptionStep.getStepData());
        outState.putBooleanArray(STATE_WEEK_DAYS, scheduleStep.getStepData());
        outState.putString(STATE_FREQUENCY, frequencyStep.getStepData());
        outState.putString(STATE_LOCATION, locationStep.getStepData());
        outState.putString(STATE_IMPORTANCE, importanceStep.getStepData());

        // IMPORTANT: The call to super method must be here at the end
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if(savedInstanceState.containsKey(STATE_GOAL)) {
            String title = savedInstanceState.getString(STATE_GOAL);
            goalStep.restoreStepData(title);
        }

        if(savedInstanceState.containsKey(STATE_DESCRIPTION)) {
            String description = savedInstanceState.getString(STATE_DESCRIPTION);
            descriptionStep.restoreStepData(description);
        }

        if(savedInstanceState.containsKey(STATE_WEEK_DAYS)) {
            boolean[] alarmDays = savedInstanceState.getBooleanArray(STATE_WEEK_DAYS);
            scheduleStep.restoreStepData(alarmDays);
        }

        if(savedInstanceState.containsKey(STATE_FREQUENCY)) {
            String freq = savedInstanceState.getString(STATE_FREQUENCY);
            frequencyStep.restoreStepData(freq);
        }

        if(savedInstanceState.containsKey(STATE_LOCATION)) {
            String freq = savedInstanceState.getString(STATE_LOCATION);
            locationStep.restoreStepData(freq);
        }

        if(savedInstanceState.containsKey(STATE_IMPORTANCE)) {
            String freq = savedInstanceState.getString(STATE_IMPORTANCE);
            importanceStep.restoreStepData(freq);
        }

        // IMPORTANT: The call to super method must be here at the end
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static class DiscardAlarmConfirmationFragment extends DialogFragment {

        private DialogInterface.OnClickListener listener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            listener = (DialogInterface.OnClickListener) context;
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Discard the goal?")
                    .setMessage("All the information will be lost")
                    .setPositiveButton("Discard", listener)
                    .setNegativeButton("Cancel", listener)
                    .setCancelable(false);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }
    }
}