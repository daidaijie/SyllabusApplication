package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/9/26.
 */
public class TakeOutModel {
    private static TakeOutModel ourInstance = new TakeOutModel();

    public static TakeOutModel getInstance() {
        return ourInstance;
    }

    public static final String EXTRA_TAKEOUT_BEEN = "com.example.daidaijie.syllabusapplication" +
            ".model.mTakeOutInfoBeen";

    List<TakeOutInfoBean> mTakeOutInfoBeen;

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    private Gson mGson;

    private TakeOutModel() {

        mGson = new GsonBuilder()
                .setPrettyPrinting()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()
                .create();


        mSharedPreferences = App.getContext().getSharedPreferences("TakeOut", Context.MODE_PRIVATE);

        mEditor = mSharedPreferences.edit();

        String menuJson = mSharedPreferences.getString(EXTRA_TAKEOUT_BEEN, "");
        if (menuJson.isEmpty()) {
            mTakeOutInfoBeen = new ArrayList<>();
        } else {
            mTakeOutInfoBeen = mGson.fromJson(menuJson,
                    new TypeToken<List<TakeOutInfoBean>>() {
                    }.getType());
        }
    }

    public List<TakeOutInfoBean> getTakeOutInfoBeen() {
        return mTakeOutInfoBeen;
    }

    public void setTakeOutInfoBeen(List<TakeOutInfoBean> takeOutInfoBeen) {
        mEditor.putString(EXTRA_TAKEOUT_BEEN, mGson.toJson(takeOutInfoBeen));
        mEditor.commit();
        mTakeOutInfoBeen = takeOutInfoBeen;
    }

    public void save() {
        mEditor.putString(EXTRA_TAKEOUT_BEEN, mGson.toJson(mTakeOutInfoBeen));
        mEditor.commit();
    }
}
