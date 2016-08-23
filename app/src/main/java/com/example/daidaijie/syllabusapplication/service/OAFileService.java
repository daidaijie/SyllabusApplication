package com.example.daidaijie.syllabusapplication.service;

import com.example.daidaijie.syllabusapplication.bean.OAFileBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/23.
 */
public interface OAFileService {

    @FormUrlEncoded
    @POST("GetDOCAccessory")
    Observable<OAFileBean> getOAFileList(@Field("token") String token,
                                         @Field("docid") int docid);
}
