package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Adapter.AllGoalsAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllGoalsFrag extends Fragment {
    private AllGoalsAdapter adapter;
    List<CurrentGoal> clist = new ArrayList<>();

    public AllGoalsFrag() {
        //Set some default data
        initInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allgoals, container, false);

        RecyclerView rv = view.findViewById(R.id.all_goals_card_list);
        rv.setHasFixedSize(true);

        //Create a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv.setLayoutManager(layoutManager);

        adapter = new AllGoalsAdapter(clist);
        adapter.setContext(getActivity());

        rv.setAdapter(adapter);

        return view;
    }

    private void initInfo() {
        CurrentGoal cg1 = new CurrentGoal();
        cg1.setTitle("Play switch");
        cg1.setFreq("Weekly");
        cg1.setDecrip("Interesting");

        CurrentGoal cg2 = new CurrentGoal();
        cg2.setTitle("Go to grocery store");
        cg2.setFreq("Every Monday and Friday");
        cg2.setDecrip("What do you do at the grocery store? \n" +
                "You go grocery shopping!\n" +
                "Some of my student say, “I'm going shopping today.\n");

        CurrentGoal cg3 = new CurrentGoal();
        cg3.setTitle("Read Fiction Book");
        cg3.setFreq("Once a week");
        cg3.setDecrip("As you do everything you do to the glory of the One who made you\n" +
                "Cause he made you\n" +
                "To do");

        CurrentGoal cg4 = new CurrentGoal();
        cg4.setTitle("exercise");
        cg4.setFreq("Daily");
        cg4.setDecrip("Every little thing that you do\n" +
                "To bring a smile to His face\n" +
                "Tell the story of grace");

        CurrentGoal cg5 = new CurrentGoal();
        cg5.setTitle("Play the piano");
        cg5.setFreq("Twice a month");
        cg5.setDecrip("AYou may be hooking up mergers\n" +
                "Cooking up burgers\n" +
                "It is at the end of the day");

        clist.add(cg1);
        clist.add(cg2);
        clist.add(cg3);
        clist.add(cg4);
        clist.add(cg5);
    }
}
