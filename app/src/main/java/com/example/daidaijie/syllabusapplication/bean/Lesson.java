package com.example.daidaijie.syllabusapplication.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/17.
 */
public class Lesson {

    private Days days;
    private String room;
    private String credit;
    private String id;
    private String teacher;
    private String duration;
    private String name;

    private List<TimeGird> mTimeGirds;

    public Lesson() {
        mTimeGirds = new ArrayList<>();
    }

    public Days getDays() {
        return days;
    }

    public void setDays(Days days) {
        this.days = days;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class TimeGird {

        public static enum WeekEum {
            FULL,
            SINGLE,
            DOUBLE,
        }

        /**
         * 总周数16
         * 单周 周一 34
         * mWeekOfTime 0b0101_0101_0101_0101
         * mWeekDate 1
         * mTimeList "34"
         */

        //所在周数
        private long mWeekOfTime;

        //所在星期
        private int mWeekDate;

        //所在时间
        private String mTimeList;

        public long getWeekOfTime() {
            return mWeekOfTime;
        }

        public void setWeekOfTime(long weekOfTime) {
            mWeekOfTime = weekOfTime;
        }

        public int getWeekDate() {
            return mWeekDate;
        }

        public void setWeekDate(int weekDate) {
            mWeekDate = weekDate;
        }

        public String getTimeList() {
            return mTimeList;
        }

        public void setTimeList(String timeList) {
            mTimeList = timeList;
        }

        /**
         * 通用的设置
         *
         * @param sumOfweek 该课程总周数
         * @param weekEum   默认的周数类型，单周还是双周还是全部
         */
        public void setWeekOfTime(int sumOfweek, WeekEum weekEum) {
            mWeekOfTime = 0;
            switch (weekEum) {
                case FULL:
                    for (long i = 0, bit = 1; i < sumOfweek; i++, bit *= 2) {
                        mWeekOfTime += bit;
                    }
                    break;
                case SINGLE:
                    for (long i = 0, bit = 1; i < sumOfweek; i += 2, bit *= 2) {
                        mWeekOfTime += bit;
                    }
                    break;
                case DOUBLE:
                    for (long i = 1, bit = 1; i < sumOfweek; i += 2, bit *= 2) {
                        mWeekOfTime += bit;
                    }
                    break;
            }
        }
    }

    /**
     * 将获取的w0,w1,w2,w3等转化成TimeGird类型
     * 以便于使用
     */
    public void convertDays() {
        TimeGird timeGrid;

        timeGrid = convertForWn(this.getDays().getW0(), 0);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW1(), 1);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW2(), 2);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW3(), 3);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW4(), 4);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW5(), 5);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW6(), 6);
        if (timeGrid != null) mTimeGirds.add(timeGrid);
    }

    /**
     *
     */
    private TimeGird convertForWn(String timeOfWeek, int week) {
        TimeGird timeGird = new TimeGird();
        if (!timeOfWeek.trim().equals("None")) {
            timeGird.setWeekDate(week);
            if (timeOfWeek.charAt(0) == '单') {
                timeGird.setWeekOfTime(16, TimeGird.WeekEum.SINGLE);
                timeGird.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else if (timeOfWeek.charAt(0) == '双') {
                timeGird.setWeekOfTime(16, TimeGird.WeekEum.DOUBLE);
                timeGird.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else {
                timeGird.setWeekOfTime(16, TimeGird.WeekEum.FULL);
                timeGird.setTimeList(timeOfWeek);
            }
        }

        return null;
    }

    /**
     * w1 : None
     * w4 : None
     * w6 : None
     * w0 : None
     * w3 : None
     * w2 : None
     * w5 : 89
     */
    public static class Days {
        private String w1;
        private String w4;
        private String w6;
        private String w0;
        private String w3;
        private String w2;
        private String w5;

        public String getW1() {
            return w1;
        }

        public void setW1(String w1) {
            this.w1 = w1;
        }

        public String getW4() {
            return w4;
        }

        public void setW4(String w4) {
            this.w4 = w4;
        }

        public String getW6() {
            return w6;
        }

        public void setW6(String w6) {
            this.w6 = w6;
        }

        public String getW0() {
            return w0;
        }

        public void setW0(String w0) {
            this.w0 = w0;
        }

        public String getW3() {
            return w3;
        }

        public void setW3(String w3) {
            this.w3 = w3;
        }

        public String getW2() {
            return w2;
        }

        public void setW2(String w2) {
            this.w2 = w2;
        }

        public String getW5() {
            return w5;
        }

        public void setW5(String w5) {
            this.w5 = w5;
        }
    }
}