package com.example.sunchen.calendarmi.Object;

public class HistoryGoal {

    private String startTime;
    private String endTime;
    private int countComplete;
    private int countTotal;
    private String history_title;

    private String title;
    private String until;


    public HistoryGoal() {

    }

    public HistoryGoal (String until, String title) {
        this.title = title;
        this.until = until;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getTitle() {
        return title;
    }

    public String getUntil() {
        return until;
    }
    public static HistoryGoal getFromString(String s) {
        if (s.contains("null")) {
            return new HistoryGoal("No history goals", "");
        }
        String[] fields = s.split(";");
        return new HistoryGoal(fields[1], fields[0]);
    }
}
