package com.example.sunchen.calendarmi.Object;

public class TodayGoal {

    private String title;
    private String frequency;
    private String location;

    public TodayGoal(String title, String frequency, String location) {
        this.title = title;
        this.frequency = frequency;
        this.location = location;
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
}
