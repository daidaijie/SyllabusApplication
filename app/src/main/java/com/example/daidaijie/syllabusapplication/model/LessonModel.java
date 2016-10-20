package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by daidaijie on 2016/8/31.
 * 管理所有课程的类
 */
public class LessonModel {

    private HashMap<Long, Lesson> mLessonHashMap;

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
        if (!User.getInstance().getCurrentAccount().trim().isEmpty()){
            setCurrentLessonModel(User.getInstance().getCurrentAccount());
        }
    }

    public void setCurrentLessonModel(String account) {
        mSharedPreferences = App.getContext().getSharedPreferences(account + "Lessons", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        String lessonsJsonString = mSharedPreferences.getString(EXTRA_LESSONS, "");
        if (lessonsJsonString.isEmpty()) {
            mLessonHashMap = new HashMap<>();
            save();
        } else {
            mLessonHashMap = GsonUtil.getDefault().fromJson(lessonsJsonString, new TypeToken<
                    HashMap<Long, Lesson>>() {
            }.getType());
        }
    }


    public void addLesson(Lesson lesson) {
        mLessonHashMap.put(lesson.getLongID(), lesson);
    }

    public Lesson getLesson(long id) {
        return mLessonHashMap.get(id);
    }

    public void removeSystemLesson(Semester semester) {
        Iterator<Map.Entry<Long, Lesson>> it = mLessonHashMap.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry<Long, Lesson> entry = it.next();
            long id = entry.getKey();
            Lesson lesson = entry.getValue();
            if (lesson.getTYPE() == Lesson.TYPE_SYSTEM) {
                if (mLessonHashMap.get(id).getSemester().equals(semester)) {
                    mLessonHashMap.remove(id);
                }
            }
        }
        save();
    }

    public void save() {
        mEditor.putString(EXTRA_LESSONS, GsonUtil.getDefault().toJson(mLessonHashMap));
        mEditor.commit();
    }


}
