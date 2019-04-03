package com.example.sunchen.calendarmi.Object;

public class CurrentGoal {
    private String decrip = "......";
    private String freq = "Daily";
    private String title = "Water Plant";

    public CurrentGoal(String decrip, String freq, String title) {
        this.decrip = decrip;
        this.freq = freq;
        this.title = title;
    }

    public CurrentGoal() {

    }

    public void setDecrip(String decrip) {
        this.decrip = decrip;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecrip() {
        return decrip;
    }

    public String getFreq() {
        return freq;
    }

    public String getTitle() {
        return title;
    }
}
