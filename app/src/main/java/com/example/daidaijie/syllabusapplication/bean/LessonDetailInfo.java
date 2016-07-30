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
     * student : [{"major":"计算机科学与技术(2012)","gender":"男","number":"2012072033","name":"刘逸森"},{"major":"计算机科学与技术(2012)","gender":"男","number":"2012081028","name":"林嘉森"},{"major":"计算机科学与技术(2012)","gender":"男","number":"2012094034","name":"周起超"},{"major":"计算机科学与技术（教育部卓越工程师班）(2013)","gender":"男","number":"2013101001","name":"蔡艺逸"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101002","name":"曾家辉"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101006","name":"陈嘉俊"},{"major":"计算机科学与技术（教育部卓越工程师班）(2013)","gender":"男","number":"2013101012","name":"程荣标"},{"major":"计算机科学与技术(2013)","gender":"女","number":"2013101017","name":"侯雪雯"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101020","name":"蓝海辉"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101024","name":"李双双"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101025","name":"李文龙"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101026","name":"李晓鹏"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101028","name":"李宇杰"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101030","name":"林子伦"},{"major":"计算机科学与技术（教育部卓越工程师班）(2013)","gender":"男","number":"2013101031","name":"刘立泽"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101032","name":"刘祥鹏"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101033","name":"刘旭"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101034","name":"刘远深"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101035","name":"罗振宇"},{"major":"市场营销(2013)","gender":"男","number":"2013101038","name":"万琛"},{"major":"计算机科学与技术(2013)","gender":"女","number":"2013101039","name":"王妙娜"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101042","name":"魏浩霖"},{"major":"计算机科学与技术(2013)","gender":"女","number":"2013101044","name":"吴雪莹"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101049","name":"叶泽泉"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101050","name":"张广辉"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101054","name":"张雄峰"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101055","name":"郑高鹏"},{"major":"计算机科学与技术（教育部卓越工程师班）(2013)","gender":"男","number":"2013101056","name":"郑贤生"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101057","name":"郑永铿"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101059","name":"郑卓"},{"major":"计算机科学与技术(2013)","gender":"男","number":"2013101062","name":"庄健春"}]
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
