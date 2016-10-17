package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu.OfficeAutomationFragment;

/**
 * Created by daidaijie on 2016/8/23.
 */
public class OAPagerAdapter extends FragmentStatePagerAdapter {

    public OAPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return OfficeAutomationFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
