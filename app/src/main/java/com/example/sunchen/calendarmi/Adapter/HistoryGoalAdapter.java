package com.example.sunchen.calendarmi.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.alespero.expandablecardview.ExpandableCardView;
import com.example.sunchen.calendarmi.Object.CurrentGoal;
import com.example.sunchen.calendarmi.Object.HistoryGoal;
import com.example.sunchen.calendarmi.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryGoalAdapter extends RecyclerView.Adapter<HistoryGoalAdapter.HistoryGoalViewHolder>  {
    private List<HistoryGoal> historyGoalList;

    public HistoryGoalAdapter(List<HistoryGoal> list) {
        this.historyGoalList = list;
    }

    @NonNull
    @Override
    public HistoryGoalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.cardview_history,viewGroup,false);
        return new HistoryGoalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryGoalViewHolder historyGoalViewHolder, int i) {
        HistoryGoal cg = historyGoalList.get(i);

        historyGoalViewHolder.title_history.setTitle(cg.getHistory_title());
        historyGoalViewHolder.period.setText(cg.getPeriod());
        historyGoalViewHolder.avg_time.setText(cg.getAvg_time());
    }

    @Override
    public int getItemCount() {
        return historyGoalList.size();
    }

    class HistoryGoalViewHolder extends RecyclerView.ViewHolder{
        protected RatingBar ratingbar;
        protected ExpandableCardView title_history;
        protected TextView period;
        protected TextView avg_time;

        public HistoryGoalViewHolder(@NonNull View itemView) {
            super(itemView);
            title_history = itemView.findViewById(R.id.history_profile_card);
            ratingbar = itemView.findViewById(R.id.rating_bar_history);
            period = itemView.findViewById(R.id.period_history);
            avg_time = itemView.findViewById(R.id.avg_time_history);
        }
    }
}
