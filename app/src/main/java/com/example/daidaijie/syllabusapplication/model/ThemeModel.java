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
        mThemeBeen.add(new ThemeBean(R.color.material_green_500, R.style.AppThemeGreen));
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
