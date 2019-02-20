package com.example.sunchen.calendarmi.Object;

public class HistoryGoal {
    private String period;
    private String avg_time;
    private String history_title;

    public HistoryGoal() {

    }

    public HistoryGoal (String period, String time, String history_title) {
        this.period = period;
        this.avg_time = time;
        this.history_title = history_title;
    }

    public void setHistory_title(String history_title) {
        this.history_title = history_title;
    }

    public void setAvg_time(String avg_time) {
        this.avg_time = avg_time;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getAvg_time() {
        return avg_time;
    }

    public String getPeriod() {
        return period;
    }

    public String getHistory_title() {
        return history_title;
    }
}
