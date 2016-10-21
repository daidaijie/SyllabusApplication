package com.example.daidaijie.syllabusapplication.schoolDynamatic.circle;

import com.example.daidaijie.syllabusapplication.bean.PostListBean;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/21.
 */

public interface ISchoolCircleModel {

    Observable<List<PostListBean>> getCircleListFromNet();

    Observable<List<PostListBean>> loadCircleListFromNet();
}
