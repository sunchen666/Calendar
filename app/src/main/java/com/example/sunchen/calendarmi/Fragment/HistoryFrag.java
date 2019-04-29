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
        adapter.setContext(getActivity());

        rv.setAdapter(adapter);

        return view;
    }

    private void initInfo() {
        HistoryGoal hg1 = new HistoryGoal();
        hg1.setTitle("Play switch");
        hg1.setUntil("04/20/2022");

        HistoryGoal hg2 = new HistoryGoal();
        hg2.setTitle("Prepare for interviews of full-time jobs");
        hg2.setUntil("2017.3.5");

        HistoryGoal hg3 = new HistoryGoal();
        hg3.setTitle("Run from home to College");
        hg3.setUntil("2018.5.5");

        clist.add(hg1);
        clist.add(hg2);
        clist.add(hg3);
    }

}
