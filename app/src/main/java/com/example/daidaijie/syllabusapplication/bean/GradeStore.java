package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by daidaijie on 2016/10/16.
 */

public class GradeStore extends RealmObject {

    private double credit;

    private double gpa;

    private int size;

    private RealmList<SemesterGrade> mSemesterGrades;

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public RealmList<SemesterGrade> getSemesterGrades() {
        return mSemesterGrades;
    }

    public void setSemesterGrades(RealmList<SemesterGrade> semesterGrades) {
        mSemesterGrades = semesterGrades;
    }
}
