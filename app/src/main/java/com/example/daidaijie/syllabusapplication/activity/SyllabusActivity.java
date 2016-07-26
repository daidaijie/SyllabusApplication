package com.example.daidaijie.syllabusapplication.activity;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SyllabusPagerAdapter;
import com.example.daidaijie.syllabusapplication.presenter.SyllabusMainPresenter;
import com.example.daidaijie.syllabusapplication.view.ISyllabusMainView;
import com.example.daidaijie.syllabusapplication.widget.SyllabusViewPager;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;

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


    private RelativeLayout navHeadRelativeLayout;
    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;

    private SyllabusPagerAdapter syllabusPagerAdapter;

    private int pageIndex;

    private int deviceWidth;
    private int devideHeight;

    private final static String TAG = "SyllabusActivity";

    private final static String SAVED_PAGE_POSITION = "pagePositon";

    private SyllabusMainPresenter mSyllabusMainPresenter = new SyllabusMainPresenter();

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

        deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
        devideHeight = getWindowManager().getDefaultDisplay().getHeight();

        setupToolbar();
        setupViewPager();

        //加载信息
        mSyllabusMainPresenter.setUserInfo();
        mSyllabusMainPresenter.loadWallpaper();

        //      设置点击navigationView菜单事件
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
        }
    }

    private void setupToolbar() {
        mToolbar.setTitle("");
        setToolBarTitle("第 " + (pageIndex + 1) + " 周");
        //透明状态栏并且适应Toolbar的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup.LayoutParams layoutParams = mToolbar.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();
        }else{
            devideHeight -= getStatusBarHeight();
        }
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

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;

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
    public void setBackground(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mMainRootLayout.setBackground(drawable);
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
                    mToolbar.setBackgroundColor(ColorUtils.setAlphaComponent(
                            getResources().getColor(R.color.colorPrimary)
                            , 192));
                    navHeadRelativeLayout.setBackgroundColor(R.color.colorPrimary);
                }
            }
        });
    }

    public void setViewPagerEnable(boolean enable) {
        mSyllabusViewPager.setScrollable(enable);
    }

    @Override
    public Resources getActivityResources() {
        return getResources();
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
            //配置功能
            FunctionConfig functionConfig = new FunctionConfig.Builder()
                    .setEnableCamera(false)
                    .setEnableEdit(true)
                    .setEnableCrop(true)
                    .setEnableRotate(true)
                    .setCropHeight(devideHeight)
                    .setCropWidth(deviceWidth)
                    .setEnablePreview(false)
                    .setCropReplaceSource(false)//配置裁剪图片时是否替换原始图片，默认不替换
                    .setForceCrop(true)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                    .build();


            GalleryFinal.openGallerySingle(200, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    PhotoInfo info = resultList.get(0);

                    Bitmap bitmap = BitmapFactory.decodeFile(info.getPhotoPath());
                    setBackground(bitmap);
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {

                }
            });


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        //点击后关闭drawerLayout
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}