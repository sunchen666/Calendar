package com.example.sunchen.calendarmi.Others;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.sunchen.calendarmi.R;

import razerdp.basepopup.BasePopupWindow;

public class DescriptionPopUpWIndow extends BasePopupWindow {
    private String title;
    private String description;
    private String location;
    private String frequency;
    private String importance;
    private String until;
    private String schedule;

    public DescriptionPopUpWIndow(Context context,
                                  String title,
                                  String description,
                                  String location,
                                  String frequency,
                                  String importance,
                                  String until,
                                  String schedule) {
        super(context, true);
        Log.e("checked", "1111");
        this.title = title;
        this.description = description;
        this.location = location;
        this.frequency = frequency;
        this.importance = importance;
        this.until = until;
        this.schedule = schedule;
        delayInit();
        onCreateContentView();
    }


    @Override
    public View onCreateContentView() {
        View view  = createPopupById(R.layout.description_info_layout);

        if (title != null ){
            TextView tt_view = view.findViewById(R.id.tt_dil);
            tt_view.setText(title);

            TextView des_view = view.findViewById(R.id.dd_dil);
            des_view.setText(description);

            TextView fre_view = view.findViewById(R.id.frequency_dil);
            fre_view.setText(frequency);

            TextView importance_view = view.findViewById(R.id.importance_dil);
            importance_view.setText(importance);

            TextView loc_view = view.findViewById(R.id.location_dil);
            loc_view.setText(location);

            TextView until_view = view.findViewById(R.id.until_dil);
            until_view.setText(until);

            TextView sche_view = view.findViewById(R.id.schedule_dil);
            sche_view.setText(schedule);
        }

        return view;
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
