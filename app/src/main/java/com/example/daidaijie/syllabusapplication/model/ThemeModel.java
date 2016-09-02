package com.example.daidaijie.syllabusapplication.model;

import com.example.daidaijie.syllabusapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/9/1.
 */
public class ThemeModel {

    public int style;

    public int colorPrimary;
    public int colorPrimaryDark;

    public List<ThemeBean> mThemeBeen;

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
        mThemeBeen.add(new ThemeBean(R.color.material_yellow_500, R.style.AppThemeYellow));
        mThemeBeen.add(new ThemeBean(R.color.material_amber_500, R.style.AppThemeAmber));
        mThemeBeen.add(new ThemeBean(R.color.material_orange_500, R.style.AppThemeOrange));
        mThemeBeen.add(new ThemeBean(R.color.material_deepOrange_500, R.style.AppThemeDeepOrange));
        mThemeBeen.add(new ThemeBean(R.color.material_brown_500, R.style.AppThemeBrown));
        mThemeBeen.add(new ThemeBean(R.color.material_grey_500, R.style.AppThemeGrey));
        mThemeBeen.add(new ThemeBean(R.color.material_blueGrey_500, R.style.AppThemeBlueGrey));
        mThemeBeen.add(new ThemeBean(R.color.colorBlack, R.style.AppThemeBlack));
    }

    public void changeTheme() {
        if (style == R.style.AppTheme) {
            style = R.style.AppThemeGreen;
        } else {
            style = R.style.AppTheme;
        }
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
