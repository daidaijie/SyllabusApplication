package com.example.daidaijie.syllabusapplication.bean;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by daidaijie on 2016/10/14.
 */

public class SemesterGrade extends RealmObject {

    private double credit;

    private double gpa;

    private RealmList<GradeBean> mGradeBeen;

    private Semester mSemester;

    @Ignore
    private Boolean isExpand;

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public RealmList<GradeBean> getGradeBeen() {
        return mGradeBeen;
    }

    public void setGradeBeen(RealmList<GradeBean> gradeBeen) {
        mGradeBeen = gradeBeen;
    }

    public Semester getSemester() {
        return mSemester;
    }

    public void setSemester(Semester semester) {
        mSemester = semester;
    }

    public Boolean getExpand() {
        return isExpand;
    }

    public void setExpand(Boolean expand) {
        isExpand = expand;
    }
}
