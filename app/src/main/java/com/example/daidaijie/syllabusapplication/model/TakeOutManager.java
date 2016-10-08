package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.TakeOutInfoBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daidaijie on 2016/9/26.
 */
public class TakeOutManager {
    private static TakeOutManager ourInstance = new TakeOutManager();

    public static TakeOutManager getInstance() {
        return ourInstance;
    }

    public static final String EXTRA_TAKEOUT_BEEN = "com.example.daidaijie.syllabusapplication" +
            ".model.mTakeOutInfoBeen";

    public static final String EXTRA_TAKEOUT_MAP = "com.example.daidaijie.syllabusapplication" +
            ".model.mIntegerMap";

    List<TakeOutInfoBean> mTakeOutInfoBeen;

    Map<String, Integer> mIntegerMap;

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    private Gson mGson;

    private TakeOutManager() {

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

        String mapJson = mSharedPreferences.getString(EXTRA_TAKEOUT_MAP, "");
        if (mapJson.isEmpty()) {
            mIntegerMap = new HashMap<>();
        } else {
            mIntegerMap = mGson.fromJson(mapJson, new TypeToken<Map<String, Integer>>() {
            }.getType());
        }
    }

    public List<TakeOutInfoBean> getTakeOutInfoBeen() {
        return mTakeOutInfoBeen;
    }

    public void setTakeOutInfoBeen(List<TakeOutInfoBean> takeOutInfoBeen) {
        mIntegerMap.clear();
        for (int i = 0; i < takeOutInfoBeen.size(); i++) {
            mIntegerMap.put(takeOutInfoBeen.get(i).getObjectId(), i);
        }
        mEditor.putString(EXTRA_TAKEOUT_BEEN, mGson.toJson(takeOutInfoBeen));
        mEditor.putString(EXTRA_TAKEOUT_MAP, mGson.toJson(mIntegerMap));
        mEditor.commit();
        mTakeOutInfoBeen = takeOutInfoBeen;
    }

    public void save() {
        mEditor.putString(EXTRA_TAKEOUT_BEEN, mGson.toJson(mTakeOutInfoBeen));
        mEditor.commit();
    }

    public TakeOutInfoBean getBeanByID(String objectID) {
        if (mIntegerMap.get(objectID) != null) {
            return mTakeOutInfoBeen.get(mIntegerMap.get(objectID));
        }
        return null;
    }

}
