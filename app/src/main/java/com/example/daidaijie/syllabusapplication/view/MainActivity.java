package com.example.daidaijie.syllabusapplication.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.example.daidaijie.syllabusapplication.R;

public class MainActivity extends AppCompatActivity {


    private ViewPager syllabusViewPager;
    private SyllabusPagerAdapter syllabusPagerAdapter;
    private Toolbar mToolbar;

    private LinearLayout mMainLinearLayout;

    private final static String SAVED_PAGE_POSITION = "pagePositon";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainLinearLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
        syllabusViewPager = (ViewPager) findViewById(R.id.syllabusViewPager);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);

        FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        syllabusViewPager.setAdapter(syllabusPagerAdapter);

        int pageIndex = 0;
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION);
        }

        mToolbar.setTitle("第 " + (pageIndex + 1) + " 周");
        mMainLinearLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background4));
        BitmapDrawable drawable = (BitmapDrawable) mMainLinearLayout.getBackground();

        Palette.from(drawable.getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch()  ;
                mToolbar.setBackgroundColor(swatch.getRgb());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(swatch.getRgb());
                }
            }
        });

        syllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mToolbar.setTitle("第 " + (position + 1) + " 周");
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