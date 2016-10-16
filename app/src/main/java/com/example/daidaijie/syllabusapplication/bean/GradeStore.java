package com.example.daidaijie.syllabusapplication.bean;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by daidaijie on 2016/10/16.
 */

public class GradeStore extends RealmObject {

    private double credit;

    private double gpa;

    private int size;

    private RealmList<SemesterGrade> mSemesterGrades;

    @Ignore
    private Map<Semester, SemesterGrade> mSemesterGradeMap;

    public void converMap() {
        if (mSemesterGradeMap == null) {
            mSemesterGradeMap = new HashMap<>();
        }
        mSemesterGradeMap.clear();
        for (SemesterGrade grades : mSemesterGrades) {
            mSemesterGradeMap.put(grades.getSemester(), grades);
        }
    }

    public Map<Semester, SemesterGrade> getSemesterGradeMap() {
        return mSemesterGradeMap;
    }

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
