package com.example.daidaijie.syllabusapplication.service;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/5.
 */
public interface LibraryPageService {

    @Headers({
            "Accept" + ": " + "text/javascript, text/html, application/xml, text/xml, */*",
            "Accept-Encoding" + ": " + "gzip, deflate, sdch",
            "Accept-Language" + ": " + "zh-CN,zh;q=0.8",
            "Connection" + ": " + "keep-alive",
            "Cookie" + ": " + "ASP.NET_SessionId=u3oluo553bxvso2nxcqd31i0",
            "Host" + ": " + "opac.lib.stu.edu.cn:83",
            "X-Prototype-Version" + ": " + "1.5.0",
            "X-Requested-With:" + ": " + "XMLHttpRequest"
    })
    @GET("showpageforlucenesearchAjax.aspx")
    Observable<String> getLibrary(@Query(value = "anywords", encoded = true) String anywords,
                                  @Query("_")String what);

}
