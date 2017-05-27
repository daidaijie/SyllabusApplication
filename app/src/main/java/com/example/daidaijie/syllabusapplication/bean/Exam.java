package com.example.daidaijie.syllabusapplication.bean;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by daidaijie on 2016/8/3.
 */
public class Exam extends RealmObject implements Serializable {

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

    private Semester mSemester;

    public Semester getSemester() {
        return mSemester;
    }

    public void setSemester(Semester semester) {
        mSemester = semester;
    }

    @PrimaryKey
    private String exam_class_number;

    private String exam_stu_position;
    private String exam_time;
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

    public String getTrueName() {
        int startIndex = 0;
        int endIndex = exam_class.length();

        int index = exam_class.indexOf("]");
        if (index != -1) {
            startIndex = index + 1;
        }

        index = exam_class.lastIndexOf("[");
        if (index != -1 && index > startIndex) {
            endIndex = index;
        }
        return exam_class.substring(startIndex, endIndex);
    }

    public String getTrueTime() {
        int index = exam_time.indexOf("(");
        if (index != -1) {
            return exam_time.substring(index + 1, exam_time.length() - 1);
        } else {
            return exam_time;
        }
    }

    public String getTruePreTime() {
        int index = exam_time.indexOf("(");
        if (index != -1) {
            return exam_time.substring(0, index);
        } else {
            return exam_time;
        }
    }

    public String getStartTime() {
        String time = getTrueTime();
        int index = time.lastIndexOf("-");
        return time.substring(0, index);
    }

    public String getPrettyTime() {
        int index = exam_time.indexOf("(");
        if (index != -1) {
            return exam_time.substring(0, index) + "\n"
                    + exam_time.substring(index + 1, exam_time.length() - 1);
        } else {
            return exam_time;
        }

    }

    public DateTime getExamTime() {
        DateTime examTime = DateTime.parse(getStartTime(), DateTimeFormat.forPattern(
                "yyyy.MM.dd  HH:mm"
        ));
        examTime = new DateTime(examTime, DateTimeZone.forOffsetHours(8));
        return examTime;
    }

}
