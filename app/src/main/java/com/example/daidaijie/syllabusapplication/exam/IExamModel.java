package com.example.daidaijie.syllabusapplication.exam;

import com.example.daidaijie.syllabusapplication.bean.Exam;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/14.
 */

public interface IExamModel {

    Observable<List<Exam>> getExamFromMemory();

    Observable<List<Exam>> getExamFromDisk();

    Observable<List<Exam>> getExamFromNet();

    Observable<List<Exam>> getExamFromCache();

    Exam getExamInList(int position);

}
