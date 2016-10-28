package com.example.daidaijie.syllabusapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu.SchoolDymaticFragment;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.mainmenu.StuCircleFragment;

/**
 * Created by daidaijie on 2016/7/17.
 */

public class StuCirclePagerAdapter extends FragmentStatePagerAdapter {
    public StuCirclePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return SchoolDymaticFragment.newInstance();
        } else if (position == 1) {
            return StuCircleFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
