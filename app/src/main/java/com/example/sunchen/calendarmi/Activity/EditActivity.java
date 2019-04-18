package com.example.sunchen.calendarmi.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.PreferenceSteps.FrequencyStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalDescriptionStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalLocationStep;
import com.example.sunchen.calendarmi.PreferenceSteps.GoalTitleStep;
import com.example.sunchen.calendarmi.PreferenceSteps.ImportanceStep;
import com.example.sunchen.calendarmi.PreferenceSteps.ScheduleStep;
import com.example.sunchen.calendarmi.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditActivity extends AppCompatActivity implements StepperFormListener, DialogInterface.OnClickListener   {
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
    private FirebaseAuth mAuth;

    static final int EDIT_GOAL_REQUEST_CODE = 111;  // The request code for entering to edit activity
    OkHttpClient client = new OkHttpClient();
    private User currentUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        String[] stepTitles = getResources().getStringArray(R.array.steps_titles);
        mAuth = FirebaseAuth.getInstance();
        currentUser = new User();

        goalStep = new GoalTitleStep(stepTitles[0]);//, stepSubtitles[0]);
        descriptionStep = new GoalDescriptionStep(stepTitles[1]);//, stepSubtitles[1]);
        scheduleStep = new ScheduleStep(stepTitles[2]);//, stepSubtitles[2]);
        frequencyStep = new FrequencyStep(stepTitles[3]);
        locationStep = new GoalLocationStep(stepTitles[4]);
        importanceStep = new ImportanceStep(stepTitles[5]);

        verticalStepperForm = findViewById(R.id.stepper_form_edits);
        verticalStepperForm.setup(this, goalStep, descriptionStep, scheduleStep, frequencyStep, locationStep, importanceStep).init();

        goalStep.restoreStepData(getIntent().getStringExtra("name"));
        descriptionStep.restoreStepData(getIntent().getStringExtra("description"));
        boolean[] isTodays = new boolean[7];
        String[] days = getIntent().getStringExtra("schedule").split(",");
        for (int i = 0; i < days.length; i++) {
            if (days[i].contains("Monday")) {
                isTodays[0] = true;
            } else if (days[i].contains("Tuesday")) {
                isTodays[1] = true;
            } else if (days[i].contains("Wednesday")) {
                isTodays[2] = true;
            } else if (days[i].contains("Thursday")) {
                isTodays[3] = true;
            } else if (days[i].contains("Friday")) {
                isTodays[4] = true;
            } else if (days[i].contains("Saturday")) {
                isTodays[5] = true;
            } else if (days[i].contains("Sunday")) {
                isTodays[6] = true;
            }
        }

        scheduleStep.restoreStepData(isTodays);
        frequencyStep.restoreStepData(getIntent().getStringExtra("frequency") + ";" + getIntent().getStringExtra("until"));
        locationStep.restoreStepData(getIntent().getStringExtra("location"));
        importanceStep.restoreStepData(getIntent().getStringExtra("importance"));
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
        progressDialog.setMessage("Modifying new goal...");

        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("name", goalStep.getStepDataAsHumanReadableString());
                builder.add("description", descriptionStep.getStepDataAsHumanReadableString());
                builder.add("weekschedule", scheduleStep.getStepDataAsHumanReadableString());
                builder.add("frequency", frequencyStep.getStepDataAsHumanReadableString());
                builder.add("location", locationStep.getStepDataAsHumanReadableString());
                builder.add("importance", importanceStep.getStepDataAsHumanReadableString());

                builder.add("email", currentUser.getCurrentUser().getEmail());

//                if (mAuth.getCurrentUser().getEmail() != null) {
//                    builder.add("email", mAuth.getCurrentUser().getEmail());
//                } else {
//                    builder.add("email", "");
//                }

                String responseResult = "";
                try {
                    int url = R.string.editgoal_server_link;
                    responseResult = post(getString(url), builder.build());
                    System.out.println("responseResult: "+responseResult);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return responseResult;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(EditActivity.this, s, Toast.LENGTH_LONG).show();
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
        };

        atask.execute();
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
        new PreferenceActivity.DiscardAlarmConfirmationFragment().show(getSupportFragmentManager(), null);
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
            builder.setTitle("Discard editing the goal?")
                    .setMessage("All the information will be lost")
                    .setPositiveButton("Discard", listener)
                    .setNegativeButton("Cancel", listener)
                    .setCancelable(false);
            Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        System.out.println("before newCall");
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            System.out.println("post response: "+res);
            return res;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == EDIT_GOAL_REQUEST_CODE) {
//
//        }
    }
}
