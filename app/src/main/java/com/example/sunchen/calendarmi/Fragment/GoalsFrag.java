package com.example.sunchen.calendarmi.Fragment;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Adapter.CardPagerAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.Object.User;
import com.example.sunchen.calendarmi.Others.ShadowTransformer;
import com.example.sunchen.calendarmi.R;
import com.example.sunchen.calendarmi.Service.TodayGoalUpdateService;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GoalsFrag extends Fragment {
    private ViewPager viewPager;
    private CardPagerAdapter cardAdapter;
    private ShadowTransformer cardShadowTransformer;

    private TodayGoalUpdateService updateService;
    private BroadcastReceiver broadcastReceiver;

    final private int mInterval = 60000;
    OkHttpClient client = new OkHttpClient();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.viewPager_home_screen);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                List<String> todayGoalStrings = intent.getStringArrayListExtra("UPDATED_GOALS");
                cardAdapter.clear();
                for (String goalString : todayGoalStrings) {
                    cardAdapter.addCardItem(TodayGoal.getFromString(goalString));
                }
                cardAdapter.notifyDataSetChanged();
                viewPager.setAdapter(cardAdapter);
            }
        };

        cardAdapter = new CardPagerAdapter();
        cardAdapter.setContext(getActivity());
        List<TodayGoal> todayGoals = fetchTodayGoals();
        for (TodayGoal goal : todayGoals) {
            cardAdapter.addCardItem(goal);
        }


        cardShadowTransformer = new ShadowTransformer(viewPager, cardAdapter);
        cardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, cardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("UPDATE_ACTION");
        getActivity().getApplicationContext().registerReceiver(broadcastReceiver, filter);
    }

    private List<TodayGoal> fetchTodayGoals() {

        final List<TodayGoal> todayGoals = new ArrayList<>();
        Date date = new Date();
        System.out.println(date);
        Calendar cal = Calendar.getInstance();
        System.out.println("fd: "+cal.getFirstDayOfWeek()+" td: "+cal.get(Calendar.DAY_OF_WEEK));
        String[] weekDayStrings = getContext().getResources().getStringArray(R.array.week_days_extended);
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
                if (responseResult.endsWith("\n")) {
                    responseResult = responseResult.substring(0, responseResult.length() - 1);
                }
                String[] goalStrings = responseResult.split(";;");
                if (!responseResult.contains("null")) {
                    Arrays.sort(goalStrings, new Comparator<String>() {
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
                }

                for (String goalString : goalStrings) {
                    System.out.println(goalString);
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
                .header("Connection", "close")
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
    }
}
