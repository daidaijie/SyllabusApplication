package com.example.daidaijie.syllabusapplication;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager syllabusViewPager;
    private SyllabusPagerAdapter syllabusPagerAdapter;

    private final static String SAVED_PAGE_POSITION = "pagePositon";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        syllabusViewPager = (ViewPager) findViewById(R.id.syllabusViewPager);

        FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        syllabusViewPager.setAdapter(syllabusPagerAdapter);

        int pageIndex = 0;
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION);
        }

        setTitle("第" + (pageIndex + 1) + "周");

        syllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle("第" + (position + 1) + "周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, syllabusViewPager.getCurrentItem());
    }
}