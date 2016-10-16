package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class GradeBean extends RealmObject{

    private String class_name;
    private String class_credit;
    private String semester;
    private String class_teacher;
    private String class_grade;
    private String years;
    private String class_number;

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_credit() {
        return class_credit;
    }

    public void setClass_credit(String class_credit) {
        this.class_credit = class_credit;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(String class_teacher) {
        this.class_teacher = class_teacher;
    }

    public String getClass_grade() {
        return class_grade;
    }

    public void setClass_grade(String class_grade) {
        this.class_grade = class_grade;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getClass_number() {
        return class_number;
    }

    public void setClass_number(String class_number) {
        this.class_number = class_number;
    }


    public double getCredit() {
        return Double.parseDouble(class_credit);
    }

    public double getGrade() {
        return Double.parseDouble(class_grade);
    }

    public String getTrueName() {
        int startIndex = 0;
        int endIndex = class_name.length();

        int index = class_name.indexOf("]");
        if (index != -1) {
            startIndex = index + 1;
        }

        index = class_name.lastIndexOf("[");
        if (index != -1 && index > startIndex) {
            endIndex = index;
        }
        return class_name.substring(startIndex, endIndex);
    }
}