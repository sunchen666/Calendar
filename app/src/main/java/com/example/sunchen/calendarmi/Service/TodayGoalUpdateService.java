package com.example.sunchen.calendarmi.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TodayGoalUpdateService extends Service {

    OkHttpClient client = new OkHttpClient();
    private final int mInterval = 10000;
    private String previousTodayGoals = "";

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("create service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startUpdateTask();
        Log.i("tag", "starting service");
        System.out.println("Service started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new TodayGoalUpdateBinder();
    }

    public class TodayGoalUpdateBinder extends Binder {

        public TodayGoalUpdateService getService(){
            return TodayGoalUpdateService.this;
        }
    }

    private void startUpdateTask() {
        final Handler mHandler = new Handler();
        Runnable update = new Runnable() {
            @Override
            public void run() {
                mHandler.postDelayed(this, mInterval);
                ArrayList<String> updatedGoalStrings = fetchTodayGoalStrings();

                if (updatedGoalStrings.size() != 1 || !updatedGoalStrings.get(0).equals("no change")) {
                    Collections.sort(updatedGoalStrings, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            String importance1 = o1.split(";")[3];
                            String importance2 = o2.split(";")[3];
                            if (!importance1.equals(importance2)) {
                                if (importance1.equals("High")) {
                                    return -1;
                                } else if (importance1.equals("Low")) {
                                    return 1;
                                } else if (importance2.equals("High")) {
                                    return 1;
                                } else if (importance2.equals("Low")) {
                                    return -1;
                                }
                            }
                            return 0;
                        }
                    });

                    Intent intent = new Intent("UPDATE_ACTION");
                    intent.putStringArrayListExtra("UPDATED_GOALS", updatedGoalStrings);
                    sendBroadcast(intent);
                }
            }
        };
        new Thread(update).start();
    }

    private ArrayList<String> fetchTodayGoalStrings() {
        final ArrayList<String> todayGoalStrings = new ArrayList<>();
        Date date = new Date();
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        String[] weekDayStrings = getResources().getStringArray(R.array.week_days_extended);
        final String dayOfWeek = weekDayStrings[(cal.get(Calendar.DAY_OF_WEEK) + 5) % 7];
        final Semaphore semp = new Semaphore(0);
        @SuppressLint("StaticFieldLeak") AsyncTask<String, Integer, String> atask = new AsyncTask<String, Integer, String>() {
            @Override
            protected String doInBackground(String... strings) {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("day", dayOfWeek);
                builder.add("email", User.getCurrentUser().getEmail());
                int url = R.string.todaygoal_server_link;
                String responseResult = "";
                try {
                    responseResult = post(getString(url), builder.build());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (responseResult.equals(previousTodayGoals)) {
                    todayGoalStrings.add("no change");
                } else {
                    previousTodayGoals = responseResult;
                    for (String goalString : responseResult.split(";;")) {
                        todayGoalStrings.add(goalString);
                    }
                }
                semp.release();
                return responseResult;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
        };
        atask.execute();
        try {
            semp.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return todayGoalStrings;
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        System.out.println("before newCall");
        String res = "";
        while (res.equals("")) {
            try (Response response = client.newCall(request).execute()) {

                res = response.body().string();
                System.out.println("post response: "+res);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return res;
    }
}
