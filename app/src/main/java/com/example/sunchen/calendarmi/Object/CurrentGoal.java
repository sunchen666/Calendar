package com.example.sunchen.calendarmi.Object;

public class CurrentGoal {
    private String decrip = "......";
    private String schedule = "Daily";
    private String title = "Water Plant";

    public CurrentGoal(String title, String decrip, String schedule) {
        this.decrip = decrip;
        this.schedule = schedule;
        this.title = title;
    }

    public CurrentGoal() {

    }

    public void setDecrip(String decrip) {
        this.decrip = decrip;
    }

    public void setSchedule(String schedule) { this.schedule = schedule; }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDecrip() {
        return decrip;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getTitle() {
        return title;
    }
    public static CurrentGoal getFromString(String goalString) {
        String[] fields = goalString.split(";");
        CurrentGoal goal = new CurrentGoal();
        goal.setTitle(fields[0]);
        goal.setDecrip(fields[1]);
        goal.setSchedule(fields[2]);
        return goal;
    }
}
