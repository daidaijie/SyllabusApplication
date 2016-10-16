package com.example.daidaijie.syllabusapplication.bean;

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
}
