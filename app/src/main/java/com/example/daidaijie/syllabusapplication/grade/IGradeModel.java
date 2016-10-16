package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.bean.GradeStore;
import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface IGradeModel {

    Observable<GradeStore> getGradeStoreListFromMemory();

    Observable<GradeStore> getGradeStoreListFromDisk();

    Observable<GradeStore> getGradeStoreListFromNet();

    Observable<GradeStore> getGradeStoreListFromCache();
}
