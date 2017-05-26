package com.example.daidaijie.syllabusapplication.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daidaijie on 2016/9/1.
 */
public class ThemeUtil {

    SharedPreferences mSharedPreferences;

    SharedPreferences.Editor mEditor;

    private static final String EXTRA_THEME = "themeName";

    private int style;

    public int colorPrimary;
    public int colorPrimaryDark;

    public List<ThemeBean> mThemeBeen;
    public Map<String, ThemeBean> mThemeBeanMap;

    public String mCurrentThemeName;

    private static ThemeUtil ourInstance = new ThemeUtil();

    public static ThemeUtil getInstance() {
        return ourInstance;
    }

    private ThemeUtil() {
        mThemeBeen = new ArrayList<>();
        mThemeBeen.add(new ThemeBean("blue", R.color.material_blue_500, R.style.AppTheme));
        mThemeBeen.add(new ThemeBean("lightBlue", R.color.material_lightBlue_500, R.style.AppThemeLightBlue));
        mThemeBeen.add(new ThemeBean("indigo", R.color.material_indigo_500, R.style.AppThemeIndigo));
        mThemeBeen.add(new ThemeBean("cyan", R.color.material_cyan_700, R.style.AppThemeCyan));
        mThemeBeen.add(new ThemeBean("teal", R.color.material_teal_500, R.style.AppThemeTeal));
        mThemeBeen.add(new ThemeBean("red", R.color.material_red_500, R.style.AppThemeRed));
        mThemeBeen.add(new ThemeBean("pink", R.color.material_pink_500, R.style.AppThemePink));
        mThemeBeen.add(new ThemeBean("purple", R.color.material_purple_500, R.style.AppThemePurple));
        mThemeBeen.add(new ThemeBean("deepPurple", R.color.material_deepPurple_500, R.style.AppThemeDeepPurple));
        mThemeBeen.add(new ThemeBean("green", R.color.material_green_500, R.style.AppThemeGreen));
        mThemeBeen.add(new ThemeBean("lightGreen", R.color.material_lightGreen_500, R.style.AppThemeLightGreen));
        mThemeBeen.add(new ThemeBean("lime", R.color.material_Lime_700, R.style.AppThemeLime));
        mThemeBeen.add(new ThemeBean("classic", R.color.colorClassic, R.style.AppThemeClassic));
        mThemeBeen.add(new ThemeBean("bili", R.color.colorBili, R.style.AppThemeBili));
        mThemeBeen.add(new ThemeBean("orange", R.color.material_orange_500, R.style.AppThemeOrange));
        mThemeBeen.add(new ThemeBean("deepOrange", R.color.material_deepOrange_500, R.style.AppThemeDeepOrange));
        mThemeBeen.add(new ThemeBean("brown", R.color.material_brown_500, R.style.AppThemeBrown));
        mThemeBeen.add(new ThemeBean("grey", R.color.material_grey_500, R.style.AppThemeGrey));
        mThemeBeen.add(new ThemeBean("blueGrey", R.color.material_blueGrey_500, R.style.AppThemeBlueGrey));
        mThemeBeen.add(new ThemeBean("black", R.color.colorBlack, R.style.AppThemeBlack));

        mThemeBeanMap = new HashMap<>();
        for (ThemeBean themeBean : mThemeBeen) {
            mThemeBeanMap.put(themeBean.name, themeBean);
        }

        mSharedPreferences = App.getContext().getSharedPreferences("THEME", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mCurrentThemeName = mSharedPreferences.getString(EXTRA_THEME, "");

        if (mCurrentThemeName == null || mCurrentThemeName.isEmpty()) {
            mCurrentThemeName = "classic";
        }
        ThemeBean themeBean = mThemeBeanMap.get(mCurrentThemeName);
        style = themeBean.styleRec;
    }


    public int getStyle() {
        return style;
    }

    public void setStyle(String name) {
        mCurrentThemeName = name;
        this.style = mThemeBeanMap.get(mCurrentThemeName).styleRec;
        mEditor.putString(EXTRA_THEME, mCurrentThemeName);
        mEditor.commit();
    }

    public class ThemeBean {
        public int styleRec;
        public int colorPrimary;
        public String name;

        public ThemeBean(String name, int colorPrimary, int styleRec) {
            this.name = name;
            this.colorPrimary = colorPrimary;
            this.styleRec = styleRec;
        }
    }
}
