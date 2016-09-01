package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.R;

/**
 * Created by daidaijie on 2016/9/1.
 */
public class ThemeModel {

    public int style;

    public int colorPrimary;
    public int colorPrimaryDark;

    private static ThemeModel ourInstance = new ThemeModel();

    public static ThemeModel getInstance() {
        return ourInstance;
    }

    private ThemeModel() {
        if (Math.random() < 0.5) {
            style = R.style.AppTheme;
        } else {
            style = R.style.AppThemeGreen;
        }
    }
}
