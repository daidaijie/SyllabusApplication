package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.PostContent;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by smallfly on 16-3-27.
 */
public interface PushPostApi {

//    # 话题, 用户自发的
//            POST_TYPE_TOPIC = 1
//
//    # 宣传活动性质的(公众号推文)
//    POST_TYPE_ACTIVITY = 2  # 如果是这种类型的话, 那么客户端处理的时候就要注意把content作为文章的URL

    public static final int POST_TYPE_TOPIC = 0;
    public static final int POST_TYPE_ACTIVITY = 1;

    @POST("interaction/api/v2.1/post")
    Observable<HttpResult<Void>> post(@Body PostContent postContent);

}
