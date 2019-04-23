package com.example.sunchen.calendarmi.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

    public TodayGoalUpdateService() {
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
                List<TodayGoal> updatedGoals = fetchTodayGoals();
                mHandler.postDelayed(this, mInterval);
            }
        };
        new Thread(update).start();
    }

    private List<TodayGoal> fetchTodayGoals() {
        final List<TodayGoal> todayGoals = new ArrayList<>();
        Date date = new Date();
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        String[] weekDayStrings = getResources().getStringArray(R.array.week_days_extended);
        final String dayOfWeek = weekDayStrings[cal.get(Calendar.DAY_OF_WEEK)];
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
                String[] goalStrings = responseResult.split(";;");
                for (String goalString : goalStrings) {
                    todayGoals.add(TodayGoal.getFromString(goalString));
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
        return todayGoals;
    }

    private String post(String url, FormBody fb) throws IOException {
        Request request = new Request.Builder()
                .url(url).post(fb)
                .build();
        System.out.println("before newCall");
        String res = "";
        try (Response response = client.newCall(request).execute()) {

            res = response.body().string();
            System.out.println("post response: "+res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
