package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/7/17.
 */
public class Lesson extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;

    private int TYPE;
    private Semester mSemester;

    public final static int TYPE_SYSTEM = 0;
    public final static int TYPE_SYSTEM_OTHER = 1;
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

    @Ignore
    private Days days;

    private String room;
    private String credit;
    private String teacher;
    private String duration;
    private String name;

    private int bgColor;

    private RealmList<TimeGrid> mTimeGrids;

    public Lesson() {
        mTimeGrids = new RealmList<>();
    }

    public Days getDays() {
        return days;
    }

    public void setDays(Days days) {
        this.days = days;
    }

    public String getRoom() {
        return room == null ? "" : room;
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
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TimeGrid> getTimeGrids() {
        return mTimeGrids;
    }

    public void setTimeGrids(RealmList<TimeGrid> timeGrids) {
        mTimeGrids = timeGrids;
    }

    public void mergeTimeGrid() {
        for (int i = 0; i < mTimeGrids.size(); i++) {

            TimeGrid thisTimeGrid = mTimeGrids.get(i);

            boolean deleteFlag = false;
            for (int j = i + 1; j < mTimeGrids.size(); j++) {
                TimeGrid thatTimeGrid = mTimeGrids.get(j);

                if (thisTimeGrid.getWeekDate() == thatTimeGrid.getWeekDate()) {
                    //找出共有部分
                    StringBuilder sb = new StringBuilder();
                    for (char x : thisTimeGrid.getTimeList().toCharArray()) {
                        if (thatTimeGrid.getTimeList().contains(x + "")) {
                            sb.append(x);
                        }
                    }
                    String mergeTimeList = sb.toString();

                    //没有共有部分就继续
                    if (mergeTimeList.length() == 0) {
                        continue;
                    }

                    //消去共有部分
                    StringBuilder thisTimeSB = new StringBuilder(thisTimeGrid.getTimeList());
                    StringBuilder thatTimeSB = new StringBuilder(thatTimeGrid.getTimeList());

                    for (char x : mergeTimeList.toCharArray()) {
                        thisTimeSB.deleteCharAt(thisTimeSB.indexOf(x + ""));
                        thatTimeSB.deleteCharAt(thatTimeSB.indexOf(x + ""));
                    }
                    thisTimeGrid.setTimeList(thisTimeSB.toString());
                    thatTimeGrid.setTimeList(thatTimeSB.toString());

                    //将共有部分创建一个新的格子
                    TimeGrid mergeTimeGrid = new TimeGrid();
                    mergeTimeGrid.setTimeList(mergeTimeList);
                    mergeTimeGrid.setWeekDate(thisTimeGrid.getWeekDate());
                    mergeTimeGrid.setWeekOfTime(thisTimeGrid.getWeekOfTime() | thatTimeGrid.getWeekOfTime());
                    mTimeGrids.add(mergeTimeGrid);

                    if (thisTimeSB.length() == 0 && thatTimeSB.length() == 0) {
                        mTimeGrids.remove(thatTimeGrid);
                        deleteFlag = true;
                        break;
                    } else {
                        if (thatTimeSB.length() == 0) {
                            mTimeGrids.remove(thatTimeGrid);
                            j--;
                            continue;
                        }

                        if (thisTimeSB.length() == 0) {
                            deleteFlag = true;
                            break;
                        }
                    }
                }
            }
            if (deleteFlag) {
                mTimeGrids.remove(thisTimeGrid);
                i--;
            }
        }
        for (TimeGrid timeGrid : mTimeGrids) {
            Logger.t("mTimeGrids").e(timeGrid.getWeekDate() + "\n" + timeGrid.getTimeList() + "\n" + timeGrid.getWeekOfTime());
        }
    }

    public static boolean isNull(Lesson lesson) {
        return lesson == null || lesson.getTimeGrids() == null;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public long getLongID() {
        return Long.parseLong(id);
    }

    public Semester getSemester() {
        return mSemester;
    }

    public void setSemester(Semester semester) {
        mSemester = semester;
    }

    public String getTrueName() {
        if (name.indexOf(']') != name.length()) {
            return name.substring(name.indexOf(']') + 1);
        }
        return name;
    }

    public String getTimeGridListString(String split) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mTimeGrids.size(); i++) {
            if (i != 0) sb.append(split);
            sb.append(mTimeGrids.get(i).getTimeString());
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
     * 将获取的w0,w1,w2,w3等转化成TimeGrid类型
     * 以便于使用
     */
    public void convertDays() {
        TimeGrid timeGrid;

        int index = duration.indexOf("-");
        int startWeek = Integer.parseInt(duration.substring(0, index).trim());
        int endWeek = Integer.parseInt(duration.substring(index + 1, duration.length()).trim());


        timeGrid = convertForWn(this.getDays().getW0(), 0, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW1(), 1, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW2(), 2, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW3(), 3, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW4(), 4, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW5(), 5, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);

        timeGrid = convertForWn(this.getDays().getW6(), 6, startWeek, endWeek);
        if (timeGrid != null) mTimeGrids.add(timeGrid);
        if (mTimeGrids.size() != 0) {
            Log.e(TAG, "convertDays: " + Long.toBinaryString(mTimeGrids.get(0).getWeekOfTime()));
        }
    }

    /**
     *
     */
    private TimeGrid convertForWn(String timeOfWeek, int week, int startWeek, int endWeek) {
        TimeGrid timeGrid = new TimeGrid();
        timeGrid.startWeek = startWeek;
        timeGrid.endWeek = endWeek;
        if (timeOfWeek != null && !timeOfWeek.trim().equals("None")) {
            timeGrid.setWeekDate(week);
            if (timeOfWeek.charAt(0) == '单') {
                timeGrid.setWeekOfTime(startWeek, endWeek, TimeGrid.WeekEum.SINGLE);
                timeGrid.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else if (timeOfWeek.charAt(0) == '双') {
                timeGrid.setWeekOfTime(startWeek, endWeek, TimeGrid.WeekEum.DOUBLE);
                timeGrid.setTimeList(timeOfWeek.substring(1, timeOfWeek.length()));
            } else {
                timeGrid.setWeekOfTime(startWeek, endWeek, TimeGrid.WeekEum.FULL);
                timeGrid.setTimeList(timeOfWeek);
            }
            return timeGrid;
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