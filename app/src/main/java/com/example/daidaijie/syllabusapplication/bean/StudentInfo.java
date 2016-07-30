package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/7/30.
 */
public class StudentInfo implements Serializable{
    private String major;
    private String gender;
    private String number;
    private String name;

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
