package com.example.daidaijie.syllabusapplication.event;

import com.example.daidaijie.syllabusapplication.bean.Syllabus;

/**
 * Created by daidaijie on 2016/7/20.
 */
public class SyllabusEvent {
    public Syllabus mSyllabus;

    public SyllabusEvent(Syllabus syllabus) {
        mSyllabus = syllabus;
    }
}
