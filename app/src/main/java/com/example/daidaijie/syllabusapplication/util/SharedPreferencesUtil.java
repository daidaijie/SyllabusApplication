package com.example.daidaijie.syllabusapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;

/**
 * 数据管理
 * Created by daidaijie on 2016/7/25.
 */
public class SharedPreferencesUtil {

    private static SharedPreferences mSharedPreferences;

    private static Context mContext;

    static {
        mContext = App.getContext();
        mSharedPreferences = mContext.getSharedPreferences("SyllabusApp", Context.MODE_PRIVATE);
    }

    private SharedPreferencesUtil() {
    }

    public static SharedPreferences getDefault() {
        return mSharedPreferences;
    }
}
