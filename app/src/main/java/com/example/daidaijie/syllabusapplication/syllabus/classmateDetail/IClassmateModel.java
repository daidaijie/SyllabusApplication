package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface IClassmateModel {

    void getStuDentInfoNormal(IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack);

    void getLessonNormal(IBaseModel.OnGetSuccessCallBack<Lesson> getSuccessCallBack);

    Observable<List<StudentInfo>> getStudentsFromNet();

    void searchStudentsList(String keyword, IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack, IBaseModel.OnGetFailCallBack getFailCallBack);
}
