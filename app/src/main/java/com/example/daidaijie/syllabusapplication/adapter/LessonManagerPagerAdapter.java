package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.syllabus.addlesson.AddLessonFragment;

/**
 * Created by daidaijie on 2016/7/17.
 */

public class LessonManagerPagerAdapter extends FragmentStatePagerAdapter {
    public LessonManagerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return AddLessonFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
