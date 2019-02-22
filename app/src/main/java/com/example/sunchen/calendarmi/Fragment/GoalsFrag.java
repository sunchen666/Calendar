package com.example.sunchen.calendarmi.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Adapter.CardPagerAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.Others.ShadowTransformer;
import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.Arrays;
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
        cardAdapter.addCardItem(new TodayGoal("Water Plant"));
        cardAdapter.addCardItem(new TodayGoal("Go to grocery store"));
        cardAdapter.addCardItem(new TodayGoal("Read Fiction Book"));
        cardAdapter.addCardItem(new TodayGoal("Exercise"));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        viewPager = (ViewPager)view.findViewById(R.id.viewPager_home_screen);

        cardAdapter = new CardPagerAdapter();
        initInfo();

        cardShadowTransformer = new ShadowTransformer(viewPager, cardAdapter);
        cardShadowTransformer.enableScaling(true);

        viewPager.setAdapter(cardAdapter);
        viewPager.setPageTransformer(false, cardShadowTransformer);
        viewPager.setOffscreenPageLimit(3);
        return view;
    }

}
