package com.example.daidaijie.syllabusapplication.retrofitApi;

import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.SchoolDymatic;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/14.
 */
public interface SchoolDymaticApi {

    /**
     * @param type              0(默认值) 表示结果按照活动开始时间排序返回
     *                          1 表示结果按照发布时间排序返回
     * @param activityStartTime type 为 0 时才有意义
     *                          表示服务器根据activity_start_time为界限,
     *                          比如activity_start_time为 2016/8/16 那么返回的活动将会是
     *                          那些已经开始了但是开始时间晚于或者等于2016/8/16的活动
     *                          以及未来的并未开始的活动
     * @param pageIndex         页码
     * @param pageSize          每页的结果数
     * @return
     */
    @GET("interaction/api/v2.1/activity")
    Observable<HttpResult<List<SchoolDymatic>>> getSchoolDymatic(
            @Query("type") int type,
            @Query("page_index") int pageIndex,
            @Query("page_size") int pageSize
    );
}
