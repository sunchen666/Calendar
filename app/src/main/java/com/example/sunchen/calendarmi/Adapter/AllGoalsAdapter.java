package com.example.sunchen.calendarmi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllGoalsAdapter extends RecyclerView.Adapter<AllGoalsAdapter.CurrentGoalViewHolder> {
    private List<CurrentGoal> currentGoalList;

    public AllGoalsAdapter(List<CurrentGoal> list) {
        this.currentGoalList = list;
    }

    @NonNull
    @Override
    public CurrentGoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_allgoals,viewGroup,false);
        return new CurrentGoalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllGoalsAdapter.CurrentGoalViewHolder currentGoalViewHolder, int i) {
        CurrentGoal cg = currentGoalList.get(i);

        currentGoalViewHolder.tTitle_cv.setTitle(cg.getTitle());
        currentGoalViewHolder.tDes.setText(cg.getDecrip());
        currentGoalViewHolder.tFre.setText(cg.getFreq());
    }

    @Override
    public int getItemCount() {
        return currentGoalList.size();
    }

    class CurrentGoalViewHolder extends RecyclerView.ViewHolder{
        protected ExpandableCardView tTitle_cv;
        protected TextView tDes;
        protected TextView tFre;

        public CurrentGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            tTitle_cv = itemView.findViewById(R.id.all_goals_profile_card);
            tDes = itemView.findViewById(R.id.description_long_goal);
            tFre = itemView.findViewById(R.id.frequency_long_goal);
        }
    }
}
