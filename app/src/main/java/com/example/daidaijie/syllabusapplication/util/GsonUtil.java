package com.example.daidaijie.syllabusapplication.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by daidaijie on 2016/7/22.
 */
public class GsonUtil {

    private static Gson mGson;

    public static Gson getDefault() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
        }
        return mGson;
    }

}
