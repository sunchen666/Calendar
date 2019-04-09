package com.example.sunchen.calendarmi.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sunchen.calendarmi.Activity.MainActivity;
import com.example.sunchen.calendarmi.Object.TodayGoal;
import com.example.sunchen.calendarmi.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

/*
* Refer to "https://rubensousa.github.io/2016/08/viewpagercards"
* */
public class CardPagerAdapter extends PagerAdapter implements CardAdapter {

    private List<CardView> mViews;
    private List<TodayGoal> mData;
    private float mBaseElevation;
    private Context context;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(TodayGoal item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_goal_homescreen, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView_home_screen);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(TodayGoal item, View view) {
        TextView titleTextView = (TextView) view.findViewById(R.id.title_TextView_goal_home_screen);
        titleTextView.setText(item.getTitle());

        TextView frequencyTextView = (TextView) view.findViewById(R.id.frequency_TextView_goal_home_screen);
        frequencyTextView.setText(item.getFrequency());

        TextView locationTextView = (TextView) view.findViewById(R.id.location_TextView_goal_home_screen);
        locationTextView.setText(item.getLocation());

        ShineButton sb = view.findViewById(R.id.like_button_goal_home_screen);
        if (context != null) {
            sb.init((Activity) context);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

}