package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/9/11.
 */
public class    Semester extends RealmObject{

    private long startWeekTime;

    private int startYear;

    private int season;

    public long getStartWeekTime() {
        return startWeekTime;
    }

    public void setStartWeekTime(long startWeekTime) {
        this.startWeekTime = startWeekTime;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public String getSeasonString() {
        switch (season) {
            case 1:
                return "秋季学期";
            case 2:
                return "春季学期";
            case 3:
                return "夏季学期";
        }
        return "";
    }

    public String getYearString() {
        return startYear + "-" + (startYear + 1);
    }

    public Semester() {
    }

    public Semester(int startYear, int season) {
        this.startYear = startYear;
        this.season = season;
    }

    public Semester(String yearString, String seasonString) {
        int index = yearString.indexOf("-");
        startYear = Integer.parseInt(yearString.substring(0, index));
        if (seasonString.equals("秋季学期")) {
            season = 1;
        } else if (seasonString.equals("春季学期")) {
            season = 2;
        } else if (seasonString.equals("夏季学期")) {
            season = 3;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Semester semester = (Semester) o;

        if (startYear != semester.startYear) return false;
        return season == semester.season;

    }

    @Override
    public int hashCode() {
        int result = startYear;
        result = 31 * result + season;
        return result;
    }
}
