package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.CircleBean;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by daidaijie on 16-8-9.
 * 获取最新的posts
 */
public interface CirclesService {
    // 2 为降序
    @GET("interaction/api/v2/posts?sort_type=2")
    rx.Observable<CircleBean> getCircles(@Query("count") int count, @Query("before_id") int offset);
}
