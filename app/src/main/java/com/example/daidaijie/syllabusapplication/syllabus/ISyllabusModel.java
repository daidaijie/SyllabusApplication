package com.example.daidaijie.syllabusapplication.syllabus;

import com.example.daidaijie.syllabusapplication.bean.Syllabus;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/19.
 */

public interface ISyllabusModel {

    Observable<Syllabus> getSyllabusFromMemory();

    Observable<Syllabus> getSyllabusFromDisk();

    Observable<Syllabus> getSyllabusFromNet();

    Observable<Syllabus> getSyllabusFromCache();

}
