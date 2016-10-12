package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.OAFileBean;

import java.util.List;

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
    Observable<List<OAFileBean>> getOAFileList(@Field("token") String token,
                                   @Field("docid") int docid);
}
