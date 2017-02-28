package com.example.daidaijie.syllabusapplication.syllabus.main.activity;

import android.content.Intent;
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
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.SyllabusPagerAdapter;
import com.example.daidaijie.syllabusapplication.adapter.WeekAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.dialog.WeekPickerFragment;
import com.example.daidaijie.syllabusapplication.event.SaveSyllabusEvent;
import com.example.daidaijie.syllabusapplication.event.SettingWeekEvent;
import com.example.daidaijie.syllabusapplication.event.ShowTimeEvent;
import com.example.daidaijie.syllabusapplication.event.SyllabusEvent;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;
import com.example.daidaijie.syllabusapplication.syllabus.main.fragment.SyllabusFragment;
import com.example.daidaijie.syllabusapplication.syllabus.main.fragment.SyllabusFragmentContract;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.widget.SyllabusViewPager;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;

import javax.inject.Inject;

import butterknife.BindView;

public class SyllabusActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        SyllabusContract.view, SyllabusFragmentContract.OnSyllabusFragmentCallBack, SyllabusFragment.OnLessonClickListener {

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
    @BindView(R.id.selectWeeksRecyclerView)
    RecyclerView mSelectWeeksRecyclerView;
    @BindView(R.id.selectWeeksLinearLayout)
    LinearLayout mSelectWeeksLinearLayout;
    @BindView(R.id.weekTitleLayout)
    LinearLayout mWeekTitleLayout;
    @BindView(R.id.showWeekSelectImageView)
    ImageView mShowWeekSelectImageView;
    @BindView(R.id.settingWeekLayout)
    LinearLayout mSettingWeekLayout;
    private RelativeLayout navHeadRelativeLayout;
    private SimpleDraweeView headImageDraweeView;
    private TextView nicknameTextView;
    private TextView semesterTextView;

    private SyllabusPagerAdapter syllabusPagerAdapter;
    private WeekAdapter mWeekAdapter;

    private int pageIndex;

    private final static String SAVED_PAGE_POSITION = SyllabusActivity.class.getCanonicalName() + ".pagePosition";

    @Inject
    SyllabusMainPresenter mSyllabusMainPresenter;

    private boolean singleLock = false;

    public static final int REQUEST_LESSON_DETAIL = 200;

    @Override
    protected int getContentView() {
        return R.layout.activity_syllabus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        //获取NavavNavigationView中的控件
        navHeadRelativeLayout = (RelativeLayout) mNavView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);
        semesterTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.semesterTextView);

        if (savedInstanceState != null) {
            pageIndex = savedInstanceState.getInt(SAVED_PAGE_POSITION, 0);
        }

        setupToolbar();
        setupViewPager();

        mNavView.setNavigationItemSelectedListener(this);

        /**
         * 周数设定
         */
        mWeekAdapter = new WeekAdapter(this);
        mSelectWeeksRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mSelectWeeksRecyclerView.setAdapter(mWeekAdapter);
        mWeekAdapter.setOnItemClickListener(new WeekAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                mSyllabusViewPager.setCurrentItem(position);
            }
        });


        mSettingWeekLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSyllabusMainPresenter.settingWeek(LocalDate.now(), mWeekAdapter.getSelectItem() + 1);
                showInfoMessage("已设定当前为第" + (mWeekAdapter.getSelectItem() + 1) + "周");
            }
        });

        SyllabusComponent.newInstance(mAppComponent);
        DaggerSyllabusMainComponent.builder()
                .userComponent(UserComponent.buildInstance(mAppComponent))
                .syllabusMainModule(new SyllabusMainModule(this))
                .build().inject(this);

        mSyllabusMainPresenter.start();

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

        mWeekTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSyllabusViewPager.isScrollable()) {
                    return;
                }
                if (mSelectWeeksLinearLayout.getVisibility() == View.GONE) {
                    showSelectWeekLayout(true);
                } else {
                    showSelectWeekLayout(false);
                }
            }
        });
    }

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
                pageIndex = position;
                setToolBarTitle("第 " + (position + 1) + " 周");
                mWeekAdapter.setSelectItem(position);
                mWeekAdapter.notifyDataSetChanged();
                mSelectWeeksRecyclerView.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    LoggerUtil.e("onPageScrollStateChanged: " + state);
                    EventBus.getDefault().post(new ShowTimeEvent(mSyllabusViewPager.getCurrentItem(), true));
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SyllabusComponent.destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PAGE_POSITION, mSyllabusViewPager.getCurrentItem());
    }


    private void setToolBarTitle(String title) {
        mTitleTextView.setText(title);
    }

    @Override
    public void setBackground(final Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        mMainRootLayout.setBackground(drawable);
        setMainColor(bitmap, true);
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mSyllabusViewPager, msg, SnackbarUtil.Info).show();
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
                                ThemeUtil.getInstance().colorPrimary
                                , 192));
                        navHeadRelativeLayout.setBackgroundColor(
                                ThemeUtil.getInstance().colorPrimary
                        );
                    }
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            mSyllabusMainPresenter.setWallpaper(deviceWidth, deviceHeight);
        } else if (id == R.id.nav_save_syllabus) {
            EventBus.getDefault().post(new SaveSyllabusEvent(pageIndex));
        } else if (id == R.id.nav_syllabus_collection) {
//            Intent intent = new Intent(this, SyllabusCollectionActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_add_lesson) {
//            Intent intent = new Intent(this, AddLessonActivity.class);
//            startActivity(intent);
        }
        //点击后关闭drawerLayout
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_syllabus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_show_time) {
            EventBus.getDefault().post(new ShowTimeEvent(mSyllabusViewPager.getCurrentItem()));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showSelectWeekLayout(boolean isShow) {
        mSelectWeeksLinearLayout.setVisibility(isShow ? View.VISIBLE : View.GONE);
        mShowWeekSelectImageView.setRotation(isShow ? 180.0f : 0.0f);
    }

    @Override
    public void showSemester(Semester semester) {
        semesterTextView.setText(semester.getYearString() + " " + semester.getSeasonString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LESSON_DETAIL) {
            singleLock = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (singleLock) return;
        super.onBackPressed();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showSelectWeek(SettingWeekEvent event) {
        WeekPickerFragment weekPickerFragment = new WeekPickerFragment();
        weekPickerFragment.setOnSettingWeekListener(new WeekPickerFragment.OnSettingWeekListener() {
            @Override
            public void onSettingWeek(LocalDate date, int week) {
                mSyllabusMainPresenter.settingWeek(date, week);
            }
        });
        weekPickerFragment.show(getSupportFragmentManager(),
                WeekPickerFragment.DIALOG_WEEK_PICKER);
    }

    @Override
    public void moveToWeek(int week) {
        mSyllabusViewPager.setCurrentItem(week - 1);
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        headImageDraweeView.setImageURI(Uri.parse(userInfo.getAvatar()));
        nicknameTextView.setText(userInfo.getNickname());
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mMainRootLayout,
                msg,
                SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mMainRootLayout,
                msg,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void onLoadStart() {
        mSyllabusViewPager.setScrollable(false);
        showSelectWeekLayout(false);
    }

    @Override
    public void onLoadEnd(boolean success) {
        mSyllabusViewPager.setScrollable(true);
        if (success) {
            mSyllabusMainPresenter.loadUserInfo();
            EventBus.getDefault().post(new SyllabusEvent(mSyllabusViewPager.getCurrentItem()));
        }
    }

    @Override
    public boolean onLessonClick() {
        if (singleLock) {
            return false;
        } else {
            singleLock = true;
            showSelectWeekLayout(false);
            return true;
        }
    }
}