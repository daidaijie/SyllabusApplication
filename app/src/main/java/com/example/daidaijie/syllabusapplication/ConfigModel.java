package com.example.daidaijie.syllabusapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by daidaijie on 2016/10/20.
 */

public class ConfigModel implements IConfigModel {

    private String FOLDER_NAME = "config";

    private String EXTRA_WALL_PAPER = LoginModel.class.getCanonicalName() + ".wallPaper";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public ConfigModel() {
        sharedPreferences = App.getContext().getSharedPreferences(FOLDER_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public String getWallPaper() {
        return sharedPreferences.getString(EXTRA_WALL_PAPER, "");

    }

    @Override
    public void setWallPaper(String filePath) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences(FOLDER_NAME, Context.MODE_PRIVATE);
        editor.putString(EXTRA_WALL_PAPER, filePath);
        editor.commit();
    }
}
