package com.example.sunchen.calendarmi.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Adapter.CardPagerAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.Others.ShadowTransformer;
import com.example.sunchen.calendarmi.R;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class GoalsFrag extends Fragment {
    private ViewPager viewPager;
    private CardPagerAdapter cardAdapter;
    private ShadowTransformer cardShadowTransformer;

    private void initInfo() {
        cardAdapter.addCardItem(new TodayGoal("Play switch", "Daily", "Home"));
//        fetchTodayGoals()

        cardAdapter.addCardItem(new TodayGoal("Water Plant", "Weekly", "Home"));
        cardAdapter.addCardItem(new TodayGoal("Go to grocery store", "Daily", "Every places"));
        cardAdapter.addCardItem(new TodayGoal("Read Fiction Book", "Daily", "Home"));
        cardAdapter.addCardItem(new TodayGoal("Exercise", "Weekly", "Working place"));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.viewPager_home_screen);

        cardAdapter = new CardPagerAdapter();
        cardAdapter.setContext(getActivity());
        initInfo();

        cardShadowTransformer = new ShadowTransformer(viewPager, cardAdapter);
        cardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, cardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);

        return view;
    }

//    private List<TodayGoal> fetchTodayGoals() {
//        Date date = new Date();
//        System.out.println(date);
//        Calendar cal = Calendar.getInstance();
//        int dayOfWeek = cal.DAY_OF_WEEK;
//
//    }

}
