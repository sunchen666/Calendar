package com.example.sunchen.calendarmi.Fragment;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GoalsFrag extends Fragment {
    public List<String> goalsText = Arrays.asList(new String[]{"Go to the Gym", "Read Books", "Go to Bed Early"});
    private int[] items = new int[] {R.layout.goal_item1, R.layout.goal_item2, R.layout.goal_item3};
    private GridView gridView;
    private List<Map<String, Object>> dataList;
    private SimpleAdapter simpAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        gridView = (GridView) view.findViewById(R.id.goal_grid); // step1
//        for (int item : items) {
//            gridView.addView(view.findViewById(item));
//        }
        dataList = new ArrayList<Map<String, Object>>(); // step2
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String goal = (String)dataList.get(i).get("text");
                ((MainActivity)getActivity()).goToPreference(goal);

            }
        });

        render();

        return view;
    }
    public void render() {
        simpAdapter = new SimpleAdapter(this.getActivity(), getData(), R.layout.goal_item1,
                new String[]{"pic", "text"}, new int[]{R.id.imageview, R.id.textview_pic});

        gridView.setAdapter(simpAdapter); // step3
    }

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < goalsText.size(); i ++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", R.drawable.picture1);
            map.put("text", goalsText.get(i));
            dataList.add(map);
        }
        return dataList;
    }
}
