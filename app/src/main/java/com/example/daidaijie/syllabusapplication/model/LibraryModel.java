package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.service.LibraryService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;

/**
 * Created by daidaijie on 2016/9/5.
 */
public class LibraryModel {

    public Retrofit mRetrofit;

    public String dt;
    public String cl;
    public String dept;
    public int dp;
    public String sm;


    public List<String> searchWords;

    public List<String> libSFs;

    public List<String> libOBs;

    public Map<Integer, List<LibraryBean>> mStoreQueryMap;

    public boolean isGetCount;

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
        dp = 10;
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

        libSFs = Arrays.asList(App.getContext().getResources().getStringArray(R.array.query_lib_sf));
        libOBs = Arrays.asList(App.getContext().getResources().getStringArray(R.array.query_lib_ob));

        mStoreQueryMap = new HashMap<>();

        isGetCount = false;
    }


    public Observable<String> getLibraryBy(String tag, String word, String sf, String ob, int position) throws UnsupportedEncodingException {

        final LibraryService libraryService = LibraryModel.getInstance().mRetrofit.create(LibraryService.class);

        Map<String, String> qureyMap = new HashMap<>();
        qureyMap.put(tag, URLEncoder.encode(word, "gb2312"));
        qureyMap.put("dt", dt);
        qureyMap.put("cl", cl);
        qureyMap.put("dept", dept);
        qureyMap.put("sf", sf);
        qureyMap.put("ob", ob);
        qureyMap.put("page", position + "");
        qureyMap.put("dp", dp + "");
        qureyMap.put("sm", sm);

        return libraryService.getLibrary(qureyMap);
    }
}
