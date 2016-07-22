package com.example.daidaijie.syllabusapplication.bean;

import com.example.daidaijie.syllabusapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daidaijie on 2016/7/19.
 * 课表类，储存一个课表
 */
public class Syllabus {

    public static final String SYLLABUS_GSON_FILE = "SyllabusGson";
    public static final String SYLLABUS_GSON = "SyllabusGson";

    public static int countOfDay = 7;
    public static int countOfTime = 13;

    private List<List<SyllabusGrid>> mSyllabusGrids;

    private static final String timeLists = "1234567890ABC";

    public static int[] bgColors = {
//            R.color.material_amber_300,

/*            R.color.material_blue_300,
            R.color.material_blue_400,
            R.color.material_blue_300,
            R.color.material_blue_600,
            R.color.material_blue_A200,
            R.color.material_blue_A400,
            R.color.material_indigo_300,
            R.color.material_indigo_400,
            R.color.material_indigo_300,
            R.color.material_indigo_600,
            R.color.material_indigo_A200,
            R.color.material_indigo_A400,*/

            //橙色系列
            /*R.color.material_orange_200,
            R.color.material_orange_300,
            R.color.material_orange_400,
            R.color.material_orange_300,
            R.color.material_orange_600,
            R.color.material_orange_A100,
            R.color.material_orange_A200,
            R.color.material_orange_A400,
            R.color.material_deepOrange_200,
            R.color.material_deepOrange_300,
            R.color.material_deepOrange_400,
            R.color.material_deepOrange_300,
            R.color.material_deepOrange_600,
            R.color.material_deepOrange_A100,
            R.color.material_deepOrange_A200,
            R.color.material_deepOrange_A400,*/
            R.color.material_cyan_300,
            R.color.material_deepOrange_300,
            R.color.material_blueGrey_300,
            R.color.material_orange_300,
            R.color.material_teal_300,
            R.color.material_deepPurple_300,
            R.color.material_red_300,
//            R.color.material_indigo_300,
            R.color.material_brown_300,
            R.color.material_lightBlue_300,
            R.color.material_pink_300,
            R.color.material_lightGreen_300,
//            R.color.material_primaryIndigo_300,
            R.color.material_grey_300,
            R.color.material_Lime_300,
            R.color.material_yellow_300,
    };


    public Syllabus() {
        mSyllabusGrids = new ArrayList<>(new ArrayList());
        for (int i = 0; i < 7; i++) {
            List<SyllabusGrid> daySyllabusGrids = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                daySyllabusGrids.add(new SyllabusGrid());
            }
            mSyllabusGrids.add(daySyllabusGrids);
        }
    }

    public List<List<SyllabusGrid>> getSyllabusGrids() {
        return mSyllabusGrids;
    }

    public void setSyllabusGrids(List<List<SyllabusGrid>> syllabusGrids) {
        mSyllabusGrids = syllabusGrids;
    }

    /**
     * 将0 1 2 3 转化为课程表上的对应时间
     *
     * @param x 课程表字符
     * @return
     */
    public static int chat2time(char x) {
        return timeLists.indexOf(x);
    }

    public static int time2char(int time) {
        if (time < 10) {
            return time + '0';
        } else if (time > 10) {
            return 'A' + (time - 11);
        } else {
            return '0';
        }
    }
}
