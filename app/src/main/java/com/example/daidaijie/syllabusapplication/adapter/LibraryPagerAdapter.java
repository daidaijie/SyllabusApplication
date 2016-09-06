package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.activity.LibraryFragment;
import com.example.daidaijie.syllabusapplication.activity.OfficeAutomationFragment;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class LibraryPagerAdapter extends FragmentStatePagerAdapter {

    private String mTag;

    private String mKeyword;

    public LibraryPagerAdapter(FragmentManager fm, String tag, String keyword) {
        super(fm);
        mTag = tag;
        mKeyword = keyword;
    }

    @Override
    public Fragment getItem(int position) {
        return LibraryFragment.newInstance(mTag, mKeyword, position);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
