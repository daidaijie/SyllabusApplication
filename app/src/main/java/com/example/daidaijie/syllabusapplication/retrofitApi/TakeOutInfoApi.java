package com.example.daidaijie.syllabusapplication.retrofitApi;


import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/24.
 */

public interface TakeOutInfoApi {

    @Headers({
            "X-Bmob-Application-Id: 2e393350bc5c4c265fcc405559503d41",
            "X-Bmob-REST-API-Key: b07c2cc5acb1c8db3d052c5b6212eefb",
    })
    @GET("classes/TakeOutMenu??order=-updatedAt")
    Observable<BmobResult<TakeOutInfoBean>> getTakeOutInfo(@Query("keys") String keys);


    @Headers({
            "X-Bmob-Application-Id: 2e393350bc5c4c265fcc405559503d41",
            "X-Bmob-REST-API-Key: b07c2cc5acb1c8db3d052c5b6212eefb",
    })
    @GET("classes/TakeOutMenu/{objectId}")
    Observable<TakeOutInfoBean> getTakeOutDetailInfo(@Path("objectId") String objectId);
}
