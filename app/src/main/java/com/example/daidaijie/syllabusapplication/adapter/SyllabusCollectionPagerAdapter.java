package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.activity.SentSyllabusFragment;
import com.example.daidaijie.syllabusapplication.activity.SyllabusCollectionFragment;

/**
 * Created by daidaijie on 2016/7/17.
 */

public class SyllabusCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public SyllabusCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return SyllabusCollectionFragment.newInstance();
        } else if (position == 1) {
            return SentSyllabusFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
