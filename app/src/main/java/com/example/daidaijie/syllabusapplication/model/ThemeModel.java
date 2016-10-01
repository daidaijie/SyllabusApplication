package com.example.daidaijie.syllabusapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/9/1.
 */
public class ThemeModel {

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    public static final String TAG = "LessonModel";

    private static final String EXTRA_Theme = "com.example.daidaijie.syllabusapplication.bean.Lesson" +
            ".LessonModel.themePostion";

    private int style;

    public int colorPrimary;
    public int colorPrimaryDark;

    public List<ThemeBean> mThemeBeen;

    public int mPosition;

    private static ThemeModel ourInstance = new ThemeModel();

    public static ThemeModel getInstance() {
        return ourInstance;
    }

    private ThemeModel() {
        mThemeBeen = new ArrayList<>();
        mThemeBeen.add(new ThemeBean(R.color.material_blue_500, R.style.AppTheme));
        mThemeBeen.add(new ThemeBean(R.color.material_lightBlue_500, R.style.AppThemeLightBlue));
        mThemeBeen.add(new ThemeBean(R.color.material_indigo_500, R.style.AppThemeIndigo));
        mThemeBeen.add(new ThemeBean(R.color.material_cyan_500, R.style.AppThemeCyan));
        mThemeBeen.add(new ThemeBean(R.color.material_teal_500, R.style.AppThemeTeal));
        mThemeBeen.add(new ThemeBean(R.color.material_red_500, R.style.AppThemeRed));
        mThemeBeen.add(new ThemeBean(R.color.material_pink_500, R.style.AppThemePink));
        mThemeBeen.add(new ThemeBean(R.color.material_purple_500, R.style.AppThemePurple));
        mThemeBeen.add(new ThemeBean(R.color.material_deepPurple_500, R.style.AppThemeDeepPurple));
        mThemeBeen.add(new ThemeBean(R.color.material_green_500, R.style.AppThemeGreen));
        mThemeBeen.add(new ThemeBean(R.color.material_lightGreen_500, R.style.AppThemeLightGreen));
        mThemeBeen.add(new ThemeBean(R.color.material_Lime_500, R.style.AppThemeLime));
        mThemeBeen.add(new ThemeBean(R.color.colorClassic, R.style.AppThemeClassic));
        mThemeBeen.add(new ThemeBean(R.color.material_amber_500, R.style.AppThemeAmber));
        mThemeBeen.add(new ThemeBean(R.color.material_orange_500, R.style.AppThemeOrange));
        mThemeBeen.add(new ThemeBean(R.color.material_deepOrange_500, R.style.AppThemeDeepOrange));
        mThemeBeen.add(new ThemeBean(R.color.material_brown_500, R.style.AppThemeBrown));
        mThemeBeen.add(new ThemeBean(R.color.material_grey_500, R.style.AppThemeGrey));
        mThemeBeen.add(new ThemeBean(R.color.material_blueGrey_500, R.style.AppThemeBlueGrey));
        mThemeBeen.add(new ThemeBean(R.color.colorBlack, R.style.AppThemeBlack));

        mSharedPreferences = App.getContext().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mPosition = mSharedPreferences.getInt(EXTRA_Theme, 0);
        style = mThemeBeen.get(mPosition).styleRec;

    }


    public int getStyle() {
        return style;
    }

    public void setStyle(int position) {
        mPosition = position;
        this.style = mThemeBeen.get(mPosition).styleRec;
        mEditor.putInt(EXTRA_Theme, mPosition);
        mEditor.commit();
    }

    public class ThemeBean {
        public int styleRec;
        public int colorPrimary;

        public ThemeBean(int colorPrimary, int styleRec) {
            this.colorPrimary = colorPrimary;
            this.styleRec = styleRec;
        }
    }
}
