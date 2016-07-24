package com.example.daidaijie.syllabusapplication.activity;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class SyllabusActivity extends AppCompatActivity {


    private ViewPager syllabusViewPager;
    private SyllabusPagerAdapter syllabusPagerAdapter;
    private Toolbar mToolbar;
    private TextView titleTextView;
    private DrawerLayout drawerLayout;

    private NavigationView mNavigationView;
    private RelativeLayout navHeadRelativeLayout;
    private LinearLayout mMainRootLayout;

    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;

    private final static String SAVED_PAGE_POSITION = "pagePositon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMainRootLayout = (LinearLayout) findViewById(R.id.mainRootLayout);
        syllabusViewPager = (ViewPager) findViewById(R.id.syllabusViewPager);
        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeadRelativeLayout = (RelativeLayout) mNavigationView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);

        /*mToolbar.inflateMenu(R.menu.menu_syllabus);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });*/
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.ShortSnackbar(mToolbar, "点击了Toolbar", SnackbarUtil.Confirm).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
            // Translucent status bar
           /* window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);*/
//            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();

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

        mToolbar.setTitle("");
        titleTextView.setText("第 " + (pageIndex + 1) + " 周");

        mMainRootLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        BitmapDrawable drawable = (BitmapDrawable) mMainRootLayout.getBackground();


        if (drawable != null) {
            Palette.from(drawable.getBitmap()).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Log.d("onGenerated: ", "onGenerated: ok");
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch != null) {
                        mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(swatch.getRgb()
                                , 192));
                        navHeadRelativeLayout.setBackgroundColor(swatch.getRgb());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(swatch.getRgb()
                                    , 188));
                        }
                    } else {
                        mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(
                                getResources().getColor(R.color.colorPrimary)
                                , 192));
                    }
                }
            });
        } else {
            mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(
                    getResources().getColor(R.color.colorPrimary)
                    , 192));
        }

        syllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleTextView.setText("第 " + (position + 1) + " 周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setSupportActionBar(mToolbar);
        //      添加toolbar drawer的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//      给drawerLayout添加监听器为toggle
        drawerLayout.addDrawerListener(toggle);

//      toggle同步状态
        toggle.syncState();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, syllabusViewPager.getCurrentItem());
    }

    public int getStatusBarHeight() {

        int result = 0;

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {

            result = getResources().getDimensionPixelSize(resourceId);

        }

        return result;

    }

    public void setDrawerLayoutInfo() {
        UserInfo userInfo = User.getInstance().getUserInfo();
        if (userInfo != null) {
            Uri uri = Uri.parse(userInfo.getAvatar());
            headImageDraweeView.setImageURI(uri);
            nicknameTextView.setText(userInfo.getNickname());
        }
    }

}