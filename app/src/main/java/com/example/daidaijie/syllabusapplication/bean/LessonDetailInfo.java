package com.example.daidaijie.syllabusapplication.bean;

import java.util.List;

/**
 * Created by daidaijie on 2016/7/30.
 */
public class LessonDetailInfo {

    /**
     * teacherName : 方若宇/于津
     * semester : 2013-2014学年秋季学期
     * stuNum : 31
     * classRoom : G座403
     * beginTime : 1-16周，周一(67节)，周四(67节)
     * className : [CST1301A]程序设计基础
     * student : 不给你看
     * classNo : 64442
     */

    private ClassInfoBean class_info;

    public ClassInfoBean getClass_info() {
        return class_info;
    }

    public void setClass_info(ClassInfoBean class_info) {
        this.class_info = class_info;
    }

    public static class ClassInfoBean {
        private String teacherName;
        private String semester;
        private int stuNum;
        private String classRoom;
        private String beginTime;
        private String className;
        private String classNo;
        /**
         * major : 计算机科学与技术(2012)
         * gender : 男
         * number : 2012072033
         * name : 刘逸森
         */

        private List<StudentInfo> student;

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getSemester() {
            return semester;
        }

        public void setSemester(String semester) {
            this.semester = semester;
        }

        public int getStuNum() {
            return stuNum;
        }

        public void setStuNum(int stuNum) {
            this.stuNum = stuNum;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassNo() {
            return classNo;
        }

        public void setClassNo(String classNo) {
            this.classNo = classNo;
        }

        public List<StudentInfo> getStudent() {
            return student;
        }

        public void setStudent(List<StudentInfo> student) {
            this.student = student;
        }

    }
}
