package com.example.sunchen.calendarmi.Adapter;


import android.support.v7.widget.CardView;

public interface CardAdapter {

    int MAX_ELEVATION_FACTOR = 20;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}
