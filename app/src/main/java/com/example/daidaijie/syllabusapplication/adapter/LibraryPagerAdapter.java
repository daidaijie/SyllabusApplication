package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu.LibraryFragment;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class LibraryPagerAdapter extends FragmentStatePagerAdapter {

    private String mTag;

    private String mKeyword;

    private String mSF;

    private String mOB;

    private int pageCount;


    public LibraryPagerAdapter(FragmentManager fm, String tag, String keyword, String SF, String OB) {
        super(fm);
        mTag = tag;
        mKeyword = keyword;
        mSF = SF;
        mOB = OB;
        pageCount = 1;
    }

    @Override
    public Fragment getItem(int position) {
        return LibraryFragment.newInstance(mTag, mKeyword, mSF, mOB, position);
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
}
