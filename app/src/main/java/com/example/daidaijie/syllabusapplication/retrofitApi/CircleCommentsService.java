package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.CommentInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/10.
 */
public interface CircleCommentsService {
    @GET("interaction/api/v2/post_comments?field=post_id")
    Observable<CommentInfo> get_comments(@Query("value") int post_id);
}
