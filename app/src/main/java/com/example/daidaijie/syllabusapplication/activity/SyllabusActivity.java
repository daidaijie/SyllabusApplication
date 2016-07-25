package com.example.daidaijie.syllabusapplication.activity;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import com.example.daidaijie.syllabusapplication.adapter.SyllabusPagerAdapter;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

public class SyllabusActivity extends BaseActivity {


    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.syllabusViewPager)
    ViewPager mSyllabusViewPager;
    @BindView(R.id.mainRootLayout)
    LinearLayout mMainRootLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private SyllabusPagerAdapter syllabusPagerAdapter;

    private RelativeLayout navHeadRelativeLayout;
    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;

    private final static String SAVED_PAGE_POSITION = "pagePositon";

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navHeadRelativeLayout = (RelativeLayout) mNavView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnackbarUtil.ShortSnackbar(mToolbar, "点击了Toolbar", SnackbarUtil.Confirm).show();
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            透明状态栏并且适应Toolbar的高度
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();

        }

        FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        mSyllabusViewPager.setAdapter(syllabusPagerAdapter);

        int pageIndex = 0;
        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION);
        }

        mToolbar.setTitle("");
        mTitleTextView.setText("第 " + (pageIndex + 1) + " 周");

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

        mSyllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleTextView.setText("第 " + (position + 1) + " 周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setSupportActionBar(mToolbar);
        //      添加toolbar drawer的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

//      给drawerLayout添加监听器为toggle
        mDrawerLayout.addDrawerListener(toggle);

//      toggle同步状态
        toggle.syncState();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, mSyllabusViewPager.getCurrentItem());
    }

    private int getStatusBarHeight() {

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