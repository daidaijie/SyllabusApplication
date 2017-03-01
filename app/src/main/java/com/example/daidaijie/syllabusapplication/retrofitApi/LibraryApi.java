package com.example.daidaijie.syllabusapplication.retrofitApi;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/5.
 */
public interface LibraryApi {

//        @Headers({
//                "Accept" + ": " + "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
//                "Accept-Encoding" + ": " + "gzip, deflate, sdch",
//                "Accept-Language" + ": " + "zh-CN,zh;q=0.8",
//                "Cache-Control" + ": " + "max-age=0",
//                "Connection" + ": " + "keep-alive",
//                "Cookie" + ": " + "ASP.NET_SessionId=u3oluo553bxvso2nxcqd31i0",
//                "Host" + ": " + "opac.lib.stu.edu.cn:83",
//                "Upgrade-Insecure-Requests" + ": " + "1",
//        })
    @GET("searchresult.aspx")
    Observable<String> getLibrary(@QueryMap(encoded = true)Map<String,String> queryMap);

}
