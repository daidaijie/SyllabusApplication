package com.example.daidaijie.syllabusapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/3.
 */
public class ExamInfo {

    @SerializedName("EXAMS")
    private List<Exam> mExams;

    public List<Exam> getExams() {
        return mExams;
    }

    public void setExams(List<Exam> exams) {
        mExams = exams;
    }
}
