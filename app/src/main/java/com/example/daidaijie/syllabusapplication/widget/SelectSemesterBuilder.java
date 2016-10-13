package com.example.daidaijie.syllabusapplication.widget;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.text.Html;

import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.di.qualifier.semester.CurrentSemester;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by daidaijie on 2016/9/11.
 */
public class SelectSemesterBuilder {

    public static LinkagePicker getSelectSemesterPicker(Activity activity) {
        Semester semester = User.getInstance().getCurrentSemester();
        int now = semester.getStartYear();

        ArrayList<String> semeberItemList = new ArrayList<>();
        semeberItemList.add("夏季学期");
        semeberItemList.add("秋季学期");
        semeberItemList.add("春季学期");

        ArrayList<String> yearList = new ArrayList<>();
        ArrayList<ArrayList<String>> semeberList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int year = now - 4 + i;
            yearList.add(year + "-" + (year + 1));
            semeberList.add(semeberItemList);
        }

        LinkagePicker picker = new LinkagePicker(activity, yearList, semeberList);
        picker.setTitleText(Html.fromHtml("<b>选择学期</b>"));
        picker.setSelectedItem(semester.getYearString(), semester.getSeasonString());
        picker.setTextSize(16);
        picker.setTextColor(ThemeModel.getInstance().colorPrimary);
        picker.setLineColor(ThemeModel.getInstance().colorPrimaryDark);

        return picker;
    }

    public static LinkagePicker newSelectSemesterPicker(Activity activity, Semester semester) {
        int now = semester.getStartYear();

        ArrayList<String> semeberItemList = new ArrayList<>();
        semeberItemList.add("夏季学期");
        semeberItemList.add("秋季学期");
        semeberItemList.add("春季学期");

        ArrayList<String> yearList = new ArrayList<>();
        ArrayList<ArrayList<String>> semeberList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int year = now - 4 + i;
            yearList.add(year + "-" + (year + 1));
            semeberList.add(semeberItemList);
        }

        LinkagePicker picker = new LinkagePicker(activity, yearList, semeberList);
        picker.setTitleText(Html.fromHtml("<b>选择学期</b>"));
        picker.setSelectedItem(semester.getYearString(), semester.getSeasonString());
        picker.setTextSize(16);
        picker.setTextColor(ThemeModel.getInstance().colorPrimary);
        picker.setLineColor(ThemeModel.getInstance().colorPrimaryDark);

        return picker;
    }
}
