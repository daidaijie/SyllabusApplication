package com.example.daidaijie.syllabusapplication.bean;

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