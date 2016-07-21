package com.example.daidaijie.syllabusapplication.view;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.daidaijie.syllabusapplication.R;

public class MainActivity extends AppCompatActivity {


    private ViewPager syllabusViewPager;
    private SyllabusPagerAdapter syllabusPagerAdapter;
    private Toolbar mToolbar;

    private LinearLayout mMainRootLayout;

    private final static String SAVED_PAGE_POSITION = "pagePositon";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainRootLayout = (LinearLayout) findViewById(R.id.mainRootLayout);
        syllabusViewPager = (ViewPager) findViewById(R.id.syllabusViewPager);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            //透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        syllabusViewPager.setAdapter(syllabusPagerAdapter);

        int pageIndex = 0;
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION);
        }

        mToolbar.setTitle("第 " + (pageIndex + 1) + " 周");
        mMainRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg6));
        BitmapDrawable drawable = (BitmapDrawable) mMainRootLayout.getBackground();

        Palette.from(drawable.getBitmap()).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                if (swatch != null) {
                    mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(swatch.getRgb()
                            , 188));
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(swatch.getRgb());
                    }*/
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