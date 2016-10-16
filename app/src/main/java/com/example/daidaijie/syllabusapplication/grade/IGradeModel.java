package com.example.daidaijie.syllabusapplication.grade;

import com.example.daidaijie.syllabusapplication.bean.SemesterGrade;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/13.
 */

public interface IGradeModel {

    Observable<List<SemesterGrade>> getGradeStoreListFromMemory();

    Observable<List<SemesterGrade>> getGradeStoreListFromDisk();

    Observable<List<SemesterGrade>> getGradeStoreListFromNet();

    Observable<List<SemesterGrade>> getGradeStoreListFromCache();
}
