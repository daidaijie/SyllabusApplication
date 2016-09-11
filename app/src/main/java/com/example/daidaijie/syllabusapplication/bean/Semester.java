package com.example.daidaijie.syllabusapplication.bean;

/**
 * Created by daidaijie on 2016/9/11.
 */
public class Semester {

    private int startYear;

    private int season;

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
        return startYear + " - " + (startYear + 1);
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
