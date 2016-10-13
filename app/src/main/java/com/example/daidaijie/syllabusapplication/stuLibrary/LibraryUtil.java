package com.example.daidaijie.syllabusapplication.stuLibrary;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.retrofitApi.LibraryApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
public class LibraryUtil {

    public List<String> searchWords;

    public List<String> libSFs;

    public List<String> libOBs;

    public LibraryUtil() {
        searchWords = Arrays.asList(App.getContext().getResources().getStringArray(R.array.query_lib));
        libSFs = Arrays.asList(App.getContext().getResources().getStringArray(R.array.query_lib_sf));
        libOBs = Arrays.asList(App.getContext().getResources().getStringArray(R.array.query_lib_ob));
    }

}
