package com.example.daidaijie.syllabusapplication;

import android.app.Application;
import android.content.Context;

/**
 * Created by daidaijie on 2016/7/24.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
