package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/17.
 */
public class Lesson implements Serializable {

    private int TYPE;

    public final static int TYPE_SYSTEM = 0;
    public final static int TYPE_DIY = 2;

    /**
     * days : {"w1":"None","w4":"None","w6":"None","w0":"None","w3":"None","w2":"None","w5":"89"}
     * room : G座303
     * credit : 2.0
     * id : 80927
     * teacher : Peterson
     * duration : 1 -16
     * name : [CST2451A]Human Computer Interaction
     */

    public static final String TAG = "Lesson";

    private Days days;
    private String room;
    private String credit;
    private String id;
    private String teacher;
    private String duration;
    private String name;

    private int bgColor;

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

    public List<TimeGird> getTimeGirds() {
        return mTimeGirds;
    }

    public void setTimeGirds(List<TimeGird> timeGirds) {
        mTimeGirds = timeGirds;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public long getIntID() {
        return Long.parseLong(id);
    }

    public String getTrueName() {
        if (name.indexOf(']') != name.length()) {
            return name.substring(name.indexOf(']') + 1);
        }
        return name;
    }

    public String getTimeGridListString(String split) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mTimeGirds.size(); i++) {
            if (i != 0) sb.append(split);
            sb.append(mTimeGirds.get(i).getTimeString());
        }
        return sb.toString();
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    /**
     * 将获取的w0,w1,w2,w3等转化成TimeGird类型
     * 以便于使用
     */
    public void convertDays() {
        TimeGird timeGrid;

        int index = duration.indexOf("-");
        int startWeek = Integer.parseInt(duration.substring(0, index).trim());
        int endWeek = Integer.parseInt(duration.substring(index + 1, duration.length()).trim());


        timeGrid = convertForWn(this.getDays().getW0(), 0, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW1(), 1, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW2(), 2, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW3(), 3, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW4(), 4, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW5(), 5, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW6(), 6, startWeek, endWeek);
        if (timeGrid != null) mTimeGirds.add(timeGrid);
        if (mTimeGirds.size() != 0) {
            Log.e(TAG, "convertDays: " + Long.toBinaryString(mTimeGirds.get(0).getWeekOfTime()));
        }
    }

    /**
     *
     */
    private TimeGird convertForWn(String timeOfWeek, int week, int startWeek, int endWeek) {
        TimeGird timeGird = new TimeGird();
        timeGird.startWeek = startWeek;
        timeGird.endWeek = endWeek;
        if (!timeOfWeek.trim().equals("None")) {
            timeGird.setWeekDate(week);
            if (timeOfWeek.charAt(0) == '单') {
                timeGird.setWeekOfTime(startWeek, endWeek, TimeGird.WeekEum.SINGLE);
                timeGird.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else if (timeOfWeek.charAt(0) == '双') {
                timeGird.setWeekOfTime(startWeek, endWeek, TimeGird.WeekEum.DOUBLE);
                timeGird.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else {
                timeGird.setWeekOfTime(startWeek, endWeek, TimeGird.WeekEum.FULL);
                timeGird.setTimeList(timeOfWeek);
            }
            return timeGird;
        }

        return null;
    }


    public static class TimeGird implements Serializable {

        public int startWeek;
        public int endWeek;

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
         * @param startWeek 该课程开始周数
         * @param endWeek   该课程结束周数
         * @param weekEum   默认的周数类型，单周还是双周还是全部
         */
        public void setWeekOfTime(int startWeek, int endWeek, WeekEum weekEum) {
            mWeekOfTime = 0;
            Log.e(TAG, "setWeekOfTime startWeek: " + startWeek);
            Log.e(TAG, "setWeekOfTime endWeek: " + endWeek);
            switch (weekEum) {
                case FULL:
                    for (long i = startWeek - 1; i < endWeek; i++) {
                        mWeekOfTime += (1 << i);
                    }
                    break;
                case SINGLE:
                    for (long i = startWeek - 1 + ((startWeek - 1) & 1); i < endWeek; i += 2) {
                        mWeekOfTime += (1 << i);
                    }
                    break;
                case DOUBLE:
                    for (long i = startWeek - ((startWeek - 1) & 1); i < endWeek; i += 2) {
                        mWeekOfTime += (1 << i);
                    }
                    break;
            }
        }

        public String getTimeString() {
            StringBuilder sb = new StringBuilder();
            sb.append(startWeek + " - " + endWeek + "周　");

            String[] weeks = {
                    "周日", "周一", "周二", "周三", "周四", "周五", "周六",
            };

            sb.append(weeks[mWeekDate]);

            boolean flag = true;
            for (int i = startWeek - 1 + ((startWeek - 1) & 1); i < endWeek; i += 2) {
                if (((mWeekOfTime >> i) & 1) != 1) {
                    flag = false;
                    break;
                }
            }
            for (int i = startWeek - ((startWeek - 1) & 1); i < endWeek; i += 2) {
                if (((mWeekOfTime >> i) & 1) == 1) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                sb.append("单");
            }

            flag = true;
            for (int i = startWeek - 1 + ((startWeek - 1) & 1); i < endWeek; i += 2) {
                if (((mWeekOfTime >> i) & 1) == 1) {
                    flag = false;
                    break;
                }
            }
            for (int i = startWeek - ((startWeek - 1) & 1); i < endWeek; i += 2) {
                if (((mWeekOfTime >> i) & 1) != 1) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                sb.append("双");
            }
            sb.append(mTimeList);
            return sb.toString();
        }
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
    public static class Days implements Serializable {
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