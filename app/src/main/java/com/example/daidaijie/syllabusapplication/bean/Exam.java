package com.example.daidaijie.syllabusapplication.bean;

import java.io.Serializable;

/**
 * Created by daidaijie on 2016/8/3.
 */
public class Exam implements Serializable{

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

    private String exam_stu_position;
    private String exam_time;
    private String exam_class_number;
    private String exam_invigilator;
    private String exam_class;
    private String exam_stu_numbers;
    private String exam_location;
    private String exam_main_teacher;
    private String exam_comment;

    public String getExam_stu_position() {
        return exam_stu_position;
    }

    public void setExam_stu_position(String exam_stu_position) {
        this.exam_stu_position = exam_stu_position;
    }

    public String getExam_time() {
        return exam_time;
    }

    public void setExam_time(String exam_time) {
        this.exam_time = exam_time;
    }

    public String getExam_class_number() {
        return exam_class_number;
    }

    public void setExam_class_number(String exam_class_number) {
        this.exam_class_number = exam_class_number;
    }

    public String getExam_invigilator() {
        return exam_invigilator;
    }

    public void setExam_invigilator(String exam_invigilator) {
        this.exam_invigilator = exam_invigilator;
    }

    public String getExam_class() {
        return exam_class;
    }

    public void setExam_class(String exam_class) {
        this.exam_class = exam_class;
    }

    public String getExam_stu_numbers() {
        return exam_stu_numbers;
    }

    public void setExam_stu_numbers(String exam_stu_numbers) {
        this.exam_stu_numbers = exam_stu_numbers;
    }

    public String getExam_location() {
        return exam_location;
    }

    public void setExam_location(String exam_location) {
        this.exam_location = exam_location;
    }

    public String getExam_main_teacher() {
        return exam_main_teacher;
    }

    public void setExam_main_teacher(String exam_main_teacher) {
        this.exam_main_teacher = exam_main_teacher;
    }

    public String getExam_comment() {
        return exam_comment;
    }

    public void setExam_comment(String exam_comment) {
        this.exam_comment = exam_comment;
    }

}
