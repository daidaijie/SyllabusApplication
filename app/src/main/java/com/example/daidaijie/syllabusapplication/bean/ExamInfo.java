package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/8/3.
 */
public class ExamInfo {


    /**
     * exam_stu_position : 48
     * exam_time : 第17周星期二第5场(2016.01.12  19:00-21:00)
     * exam_class_number : 13594
     * exam_invigilator : 屈建勤
     * exam_class : [CST3401A]软件工程[CST9301]
     * exam_stu_numbers : 60
     * exam_location : E303
     * exam_main_teacher : 蔡浩
     * exam_comment :
     */

    private List<Exam> EXAMS;

    public List<Exam> getEXAMS() {
        return EXAMS;
    }

    public void setEXAMS(List<Exam> EXAMS) {
        this.EXAMS = EXAMS;
    }

}
