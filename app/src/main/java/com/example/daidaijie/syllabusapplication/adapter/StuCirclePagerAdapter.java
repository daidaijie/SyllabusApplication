package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.activity.StuCircleFragment;
import com.example.daidaijie.syllabusapplication.activity.SyllabusFragment;

/**
 * Created by daidaijie on 2016/7/17.
 */

public class StuCirclePagerAdapter extends FragmentStatePagerAdapter {
    public StuCirclePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return StuCircleFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
