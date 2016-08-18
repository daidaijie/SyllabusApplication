package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.PostCommentBean;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/18.
 */
public interface SendCommentService {

    @POST("/interaction/api/v2/comment")
    Observable<Void> sendComment(@Body PostCommentBean postCommentBean);
}
