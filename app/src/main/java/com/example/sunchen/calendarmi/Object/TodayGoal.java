package com.example.sunchen.calendarmi.Object;

import android.os.Parcelable;

public class TodayGoal {

    private String title;
    private String frequency;
    private String location;
    private String importance;

    public TodayGoal(String title, String frequency, String location, String importance) {
        this.title = title;
        this.frequency = frequency;
        this.location = location;
        this.importance = importance;
    }
    public TodayGoal() {

    }

    public String getImportance() {
        return importance;
    }
    public String getTitle() {
        return title;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getLocation() {
        return location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public void setImportance(String importance) {this.importance = importance; }


    public static TodayGoal getFromString(String goalString) {
        System.out.println("getFromString: "+goalString);
        TodayGoal goal = new TodayGoal();
        if (goalString.contains("null")) {
            goal.setTitle("No Goal for Today");
            goal.setFrequency("");
            goal.setLocation("");
            goal.setImportance("Avg.");
        } else {
            String[] fields = goalString.split(";");
            goal.setTitle(fields[0]);
            goal.setFrequency(fields[1]);
            goal.setLocation(fields[2]);
            goal.setImportance(fields[3]);
        }

        return goal;
    }
}
