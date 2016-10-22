package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/22.
 */

public interface IClassmateModel {

    void getStuDentInfoNormal(IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack);

    Observable<List<StudentInfo>> getStudentsFromNet();
}
