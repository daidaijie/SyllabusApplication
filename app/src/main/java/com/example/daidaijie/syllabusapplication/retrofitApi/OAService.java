package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.OABean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by daidaijie on 2016/8/23.
 */
public interface OAService {

    @FormUrlEncoded
    @POST("GetDoc")
    Observable<List<OABean>> getOAInfo(@Field("token") String token,
                                       @Field("subcompany_id") int subcompanyID,
                                       @Field("keyword") String keyword,
                                       @Field("row_start") int start,
                                       @Field("row_end") int end
    );


}
