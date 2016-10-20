package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.syllabus.main.fragment.SyllabusFragment;

/**
 * Created by daidaijie on 2016/7/17.
 */

public class SyllabusPagerAdapter extends FragmentStatePagerAdapter {
    public SyllabusPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SyllabusFragment.newInstance(position );
    }

    @Override
    public int getCount() {
        return 16;
    }
}
