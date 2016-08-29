package com.example.daidaijie.syllabusapplication.bean;

import android.util.Log;

import com.example.daidaijie.syllabusapplication.activity.GradeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/8/19.
 */
public class GradeInfo {

    /**
     * GRADES : [[{"class_name":"[ELC1]英语(ELC1)","class_credit":"4.0","semester":"秋季学期","class_teacher":"马蕊",}
     * GPA : 2.8773333333333326
     */

    private double GPA;
    /**
     * class_name : [ELC1]英语(ELC1)
     * class_credit : 4.0
     * semester : 秋季学期
     * class_teacher : 马蕊
     * class_grade : 68
     * years : 2013-2014
     * class_number : 62566
     */

    private List<List<GradeBean>> GRADES;

    public double getGPA() {
        return GPA;
    }

    public void setGPA(double GPA) {
        this.GPA = GPA;
    }

    public List<List<GradeBean>> getGRADES() {
        return GRADES;
    }

    public List<Boolean> isExpands;

    public void trimList() {
        if (GRADES == null) return;
        for (int i = 0; i < GRADES.size(); i++) {
            List<GradeBean> gradeBeen = GRADES.get(i);
            if (gradeBeen.size() == 0) {
                GRADES.remove(gradeBeen);
                --i;
            }
        }

        isExpands = new ArrayList<>();
        for (int i = 0; i < GRADES.size(); i++) {
            isExpands.add(false);
        }

    }

    public void setIsExpands(boolean isExpand) {
        for (int i = 0; i < GRADES.size(); i++) {
            isExpands.set(i, isExpand);
        }
    }

    public double getSumCredit(int position) {
        if (GRADES == null) return 0.0;
        List<GradeBean> gradeBeen = GRADES.get(position);

        double sumCredit = 0.0;

        for (GradeBean gradeBean : gradeBeen) {
            sumCredit += gradeBean.getCredit();
        }

        return sumCredit;
    }

    public double getSumGpa(int position) {
        if (GRADES == null) return 0.0;
        double sumCredit = getSumCredit(position);
        if (sumCredit == 0) return 0.0;

        List<GradeBean> gradeBeen = GRADES.get(position);

        double sumGpa = 0.0;
        for (GradeBean gradeBean : gradeBeen) {
            if (gradeBean.getGrade() >= 60.0) {
                sumGpa += (gradeBean.getGrade() - 50) / 10.0 * gradeBean.getCredit();
            }
        }

        return sumGpa / sumCredit;
    }

    public int getAllSize() {
        if (GRADES == null) return 0;
        int allSize = 0;
        for (List<GradeBean> gradeBeen : GRADES) {
            if (gradeBeen != null) {
                allSize += gradeBeen.size();
            }
        }
        return allSize;
    }

    public double getAllCredit() {
        if (GRADES == null) return 0.0;
        double allCredit = 0.0;
        for (int i = 0; i < GRADES.size(); i++) {
            allCredit += getSumCredit(i);
        }
        return allCredit;
    }

    public double getAllGpa() {
        if (GRADES == null) return 0.0;
        double allCredit = getAllCredit();
        if (allCredit == 0) return 0.0;

        double allGpa = 0.0;
        for (List<GradeBean> gradeBeen : GRADES) {
            for (GradeBean gradeBean : gradeBeen) {
                if (gradeBean.getGrade() >= 60.0) {
                    allGpa += (gradeBean.getGrade() - 50) / 10.0 * gradeBean.getCredit();
                }
            }

        }
        return allGpa / allCredit;
    }

    public void setGRADES(List<List<GradeBean>> GRADES) {
        this.GRADES = GRADES;
    }

    public static class GradeBean {
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
}
