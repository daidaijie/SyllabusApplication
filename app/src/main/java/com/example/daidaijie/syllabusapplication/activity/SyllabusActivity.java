package com.example.daidaijie.syllabusapplication.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SyllabusPagerAdapter;
import com.example.daidaijie.syllabusapplication.presenter.SyllabusMainPresenter;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.view.ISyllabusMainView;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;
import com.example.daidaijie.syllabusapplication.widget.SyllabusViewPager;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;

public class SyllabusActivity extends BaseActivity implements ISyllabusMainView, NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.syllabusViewPager)
    SyllabusViewPager mSyllabusViewPager;
    @BindView(R.id.mainRootLayout)
    LinearLayout mMainRootLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private AlertDialog mLoadingDialog;

    private RelativeLayout navHeadRelativeLayout;
    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;

    private SyllabusPagerAdapter syllabusPagerAdapter;

    private int pageIndex;

    private final static String TAG = "SyllabusActivity";

    private final static String SAVED_PAGE_POSITION = "pagePositon";

    private SyllabusMainPresenter mSyllabusMainPresenter = new SyllabusMainPresenter();

    private boolean singleLock = false;


    @Override
    protected int getContentView() {
        return R.layout.activity_syllabus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSyllabusMainPresenter.attach(this);

        //获取NavavNavigationView中的控件
        navHeadRelativeLayout = (RelativeLayout) mNavView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);

        setupToolbar();
        setupViewPager();

        //加载信息
        mSyllabusMainPresenter.setUserInfo();
        mSyllabusMainPresenter.loadWallpaper(this);

        //      设置点击navigationView菜单事件
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
        }
        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this,
                getResources().getColor(R.color.colorPrimary));
    }

    private void setupToolbar() {
        setToolBarTitle("第 " + (pageIndex + 1) + " 周");
        mToolbar.setTitle("");
        setupToolbar(mToolbar);

        setSupportActionBar(mToolbar);
        //添加toolbar drawer的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void setupViewPager() {
        pageIndex = 0;
        FragmentManager manager = getSupportFragmentManager();
        syllabusPagerAdapter = new SyllabusPagerAdapter(manager);
        mSyllabusViewPager.setAdapter(syllabusPagerAdapter);
        mSyllabusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position
                    , float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setToolBarTitle("第 " + (position + 1) + " 周");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSyllabusMainPresenter.detach();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, mSyllabusViewPager.getCurrentItem());
    }


    @Override
    public void setHeadImageView(Uri uri) {
        headImageDraweeView.setImageURI(uri);
    }

    @Override
    public void setNickName(String nickName) {
        nicknameTextView.setText(nickName);
    }

    @Override
    public void setToolBarTitle(String title) {
        mTitleTextView.setText(title);
    }

    @Override
    public void setBackground(final Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mMainRootLayout.setBackground(drawable);
        setMainColor(bitmap, true);
    }

    private void setMainColor(final Bitmap bitmap, final boolean isFirst) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
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
                    if (isFirst) {
                        if (swatch == null) {
                            Bitmap bitmap2 = Bitmap.createBitmap(
                                    bitmap, 0, 0,
                                    bitmap.getWidth(), bitmap.getHeight() / 4
                            );
                            setMainColor(bitmap2, false);
                        }
                    } else {
                        mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(
                                getResources().getColor(R.color.colorPrimary)
                                , 192));
                        navHeadRelativeLayout.setBackgroundColor(getResources().getColor(
                                R.color.colorPrimary
                        ));
                    }
                }
            }
        });
    }

    public void setViewPagerEnable(boolean enable) {
        mSyllabusViewPager.setScrollable(enable);
    }

    public SyllabusMainPresenter getSyllabusMainPresenter() {
        return mSyllabusMainPresenter;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            mSyllabusMainPresenter.setWallpaper();
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, LessonManagerActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(this,ExamActivity.class);
            startActivity(intent);
        }
        //点击后关闭drawerLayout
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public int getDeviceWidth() {
        return deviceWidth;
    }

    @Override
    public int getDevideHeight() {
        return devideHeight;
    }

    @Override
    public void showSuccessSnackbar(String info) {
        SnackbarUtil.ShortSnackbar(
                mMainRootLayout,
                info,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void showFailSnackbar(String info, String again, View.OnClickListener listener) {
        SnackbarUtil.LongSnackbar(
                mMainRootLayout,
                info,
                SnackbarUtil.Alert
        ).setAction(again, listener).show();

    }

    public boolean isSingleLock() {
        return singleLock;
    }

    public void setSingleLock(boolean singleLock) {
        this.singleLock = singleLock;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: " + requestCode);
        if (requestCode == 200) {
            singleLock = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (singleLock) return;
        super.onBackPressed();
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.show();
            //要在show之后加，不然没效果
            WindowManager.LayoutParams params = mLoadingDialog.getWindow().getAttributes();
            params.width = deviceWidth * 7 / 8;
            mLoadingDialog.getWindow().setAttributes(params);
        }
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }

    }
}