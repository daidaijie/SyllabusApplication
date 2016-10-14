package com.example.daidaijie.syllabusapplication.exam.detail;

import android.text.SpannableStringBuilder;

import com.example.daidaijie.syllabusapplication.bean.Exam;

/**
 * Created by daidaijie on 2016/10/14.
 */

public interface IExamItemModel {

    Exam getExam();

    SpannableStringBuilder getExamState();
}
