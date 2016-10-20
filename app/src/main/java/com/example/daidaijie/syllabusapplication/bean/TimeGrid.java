package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/10/20.
 */

public class TimeGrid extends RealmObject implements Serializable {

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
