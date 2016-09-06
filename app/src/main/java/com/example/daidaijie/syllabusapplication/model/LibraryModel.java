package com.example.daidaijie.syllabusapplication.model;

import android.net.Uri;

import com.example.daidaijie.syllabusapplication.service.LibraryService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/5.
 */
public class LibraryModel {

    public Retrofit mRetrofit;

    public String dt;
    public String cl;
    public String dept;
    public String sf;
    public String ob;
    public int dp;
    public String sm;


    public List<String> searchWords;


    private static LibraryModel ourInstance = new LibraryModel();

    public static LibraryModel getInstance() {
        return ourInstance;
    }

    private LibraryModel() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://opac.lib.stu.edu.cn:83/opac/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        dt = "ALL";
        cl = "ALL";
        dept = "ALL";
        sf = "M_PUB_YEAR";
        ob = "DESC";
        dp = 20;
        sm = "table";


        searchWords = new ArrayList<>();
        searchWords.add("anywords");
        searchWords.add("title_f");
        searchWords.add("title");
        searchWords.add("author_f");
        searchWords.add("author");
        searchWords.add("keyword_f");
        searchWords.add("publisher_f");
        searchWords.add("clc_f");
        searchWords.add("isbn_f");
        searchWords.add("issn_f");
        searchWords.add("callno_f");
    }


    public String getQueryString(int page) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s&", URLEncoder.encode(sb.toString(), "gb2312")));
        sb.append(String.format("%s=%s&", "dt", dt));
        sb.append(String.format("%s=%s&", "cl", cl));
        sb.append(String.format("%s=%s&", "dept", dept));
        sb.append(String.format("%s=%s&", "sf", sf));
        sb.append(String.format("%s=%s&", "ob", ob));
        sb.append(String.format("%s=%s&", "page", page));
        sb.append(String.format("%s=%s&", "dp", dp));
        sb.append(String.format("%s=%s", "sm", sm));
        return URLEncoder.encode(sb.toString(), "gb2312");
    }

    public Observable<String> getLibraryBy(String tag, String word, int position) throws UnsupportedEncodingException {

        final LibraryService libraryService = LibraryModel.getInstance().mRetrofit.create(LibraryService.class);

        Map<String, String> qureyMap = new HashMap<>();
        qureyMap.put(tag, URLEncoder.encode(word,"gb2312"));
        qureyMap.put("dt", dt);
        qureyMap.put("cl", cl);
        qureyMap.put("dept", dept);
        qureyMap.put("sf", sf);
        qureyMap.put("page", position + "");
        qureyMap.put("dp", dp + "");
        qureyMap.put("sm", sm);

        return libraryService.getLibrary(qureyMap);
    }
}
