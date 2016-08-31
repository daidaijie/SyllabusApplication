package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * Created by daidaijie on 2016/8/31.
 * 管理所有课程的类
 */
public class LessonModel {

    private HashMap<Integer, Lesson> mLessonHashMap;

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    public static final String TAG = "LessonModel";

    private static final String EXTRA_LESSONS = "com.example.daidaijie.syllabusapplication.bean.Lesson" +
            ".LessonModel.mLessonHashMap";

    private static LessonModel ourInstance = new LessonModel();

    public static LessonModel getInstance() {
        return ourInstance;
    }

    private LessonModel() {
        mSharedPreferences = App.getContext().getSharedPreferences("Lessons", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        String lessonsJsonString = mSharedPreferences.getString(EXTRA_LESSONS, "");
        Log.e(TAG, "LessonModel: " + lessonsJsonString);
        if (lessonsJsonString.isEmpty()) {
            mLessonHashMap = new HashMap<>();
            save();
        } else {
            mLessonHashMap = GsonUtil.getDefault().fromJson(lessonsJsonString, new TypeToken<
                    HashMap<Integer, Lesson>>() {
            }.getType());
        }

    }


    public void addLesson(Lesson lesson) {
        mLessonHashMap.put(lesson.getIntID(), lesson);
    }

    public Lesson getLesson(int id) {
        return mLessonHashMap.get(id);
    }

    public void save() {
        mEditor.putString(EXTRA_LESSONS, GsonUtil.getDefault().toJson(mLessonHashMap));
        mEditor.commit();
    }


}
