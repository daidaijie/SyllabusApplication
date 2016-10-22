package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.base.IBaseModel;
import com.example.daidaijie.syllabusapplication.bean.StudentInfo;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class ClassmateModel implements IClassmateModel {

    List<StudentInfo> mStudentInfos;

    @Override
    public void getStuDentInfoNormal(IBaseModel.OnGetSuccessCallBack<List<StudentInfo>> getSuccessCallBack) {

    }

    @Override
    public Observable<List<StudentInfo>> getStudentsFromNet() {
        return null;
    }
}
