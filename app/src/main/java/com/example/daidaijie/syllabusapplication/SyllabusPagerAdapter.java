package com.example.daidaijie.syllabusapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by daidaijie on 2016/7/17.
 */
public class SyllabusPagerAdapter extends FragmentStatePagerAdapter{
    public SyllabusPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return BlankFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 16;
    }
}
