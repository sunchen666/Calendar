package com.example.sunchen.calendarmi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sunchen.calendarmi.Adapter.AllGoalsAdapter;
import com.example.sunchen.calendarmi.Adapter.HistoryGoalAdapter;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.HistoryGoal;
import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryFrag extends Fragment {
    private HistoryGoalAdapter adapter;
    List<HistoryGoal> clist = new ArrayList<>();

    public HistoryFrag() {
        //Set some default data
        initInfo();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView rv = view.findViewById(R.id.history_card_list);
        rv.setHasFixedSize(true);

        //Create a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        adapter = new HistoryGoalAdapter(clist);

        rv.setAdapter(adapter);

        return view;
    }

    private void initInfo() {
        HistoryGoal hg1 = new HistoryGoal();
        hg1.setHistory_title("Write a book");
        hg1.setPeriod("2016.1.5 - 2018.9.3");
        hg1.setAvg_time("44 min");

        HistoryGoal hg2 = new HistoryGoal();
        hg2.setHistory_title("Apply for internship");
        hg2.setPeriod("2017.3.5 - 2018.3.2");
        hg2.setAvg_time("30 min");

        HistoryGoal hg3 = new HistoryGoal();
        hg3.setHistory_title("Run from home to College");
        hg3.setPeriod("2018.5.5 - 2018.7.2");
        hg3.setAvg_time("31 min");

        clist.add(hg1);
        clist.add(hg2);
        clist.add(hg3);

    }

}
