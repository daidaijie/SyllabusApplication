package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Banner;
import com.example.daidaijie.syllabusapplication.bean.BannerInfo;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.model.BannerModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.service.BannerService;
import com.example.daidaijie.syllabusapplication.takeout.mainMenu.TakeOutActivity;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.widget.ItemCardLayout;
import com.example.daidaijie.syllabusapplication.widget.SelectSemesterBuiler;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.liaoinstan.springview.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.query_syllabus_button)
    LinearLayout mQuerySyllabusButton;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toOAItemLayout)
    ItemCardLayout mToOAItemLayout;
    @BindView(R.id.toSTUItemLayout)
    ItemCardLayout mToSTUItemLayout;
    @BindView(R.id.toSyllabusItemLayout)
    MaterialRippleLayout mToSyllabusItemLayout;
    @BindView(R.id.toExamCardItem)
    ItemCardLayout mToExamCardItem;
    @BindView(R.id.toGradeCardItem)
    ItemCardLayout mToGradeCardItem;
    @BindView(R.id.toEmailCardItem)
    ItemCardLayout mToEmailCardItem;


    RelativeLayout navHeadRelativeLayout;
    SimpleDraweeView headImageDraweeView;
    TextView semesterTextView;
    TextView nicknameTextView;

    public static final String TAG = "MainActivity";
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.toWifiItemLayout)
    ItemCardLayout mToWifiItemLayout;
    @BindView(R.id.toLibraryCardItem)
    ItemCardLayout mToLibraryCardItem;
    @BindView(R.id.toTakeOutCardItem)
    ItemCardLayout mToTakeOutCardItem;
    private BannerModel mBannerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CollapsingToolbarLayout.LayoutParams layoutParams
                = (CollapsingToolbarLayout.LayoutParams) mConvenientBanner.getLayoutParams();
        layoutParams.height = deviceWidth / 16 * 9;
        layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.toolbar_height);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            layoutParams.topMargin += getStatusBarHeight();
        }
        mToolbarLayout.setTitle("");

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        //添加toolbar drawer的开关
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (mNavView != null) {
            mNavView.setNavigationItemSelectedListener(this);
        }
        //获取NavavNavigationView中的控件
        navHeadRelativeLayout = (RelativeLayout) mNavView.getHeaderView(0);
        headImageDraweeView = (SimpleDraweeView) navHeadRelativeLayout.findViewById(R.id.headImageDraweeView);
        semesterTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.semesterTextView);
        nicknameTextView = (TextView) navHeadRelativeLayout.findViewById(R.id.nicknameTextView);

        setSemesterText();
        setHeadImage();
        setNiceName();


        mBannerModel = BannerModel.getInstance();

        setBannerPage(mBannerModel.mBanners);

        getBanner();

        mToWifiItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginInternetActivity.class);
                startActivity(intent);
            }
        });

        mToOAItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OfficeAutomationActivity.class);
                startActivity(intent);
            }
        });
        mToSTUItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, STUCircleActivity.class);
                startActivity(intent);
            }
        });
        mToSyllabusItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SyllabusActivity.class);
                startActivity(intent);
            }
        });

        mToLibraryCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LibraryActivity.class);
                startActivity(intent);
            }
        });
        mToExamCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExamActivity.class);
                startActivity(intent);
            }
        });
        mToGradeCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GradeActivity.class);
                startActivity(intent);
            }
        });

        mToTakeOutCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TakeOutActivity.class);
                startActivity(intent);
            }
        });
        mToEmailCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmailWebActivity.class);
                startActivity(intent);

            }
        });

//        Log.e(TAG, "onCreate: token" + User.getInstance().getUserInfo().getToken());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    private class BannerImageHolderView implements Holder<Banner> {
        SimpleDraweeView draweeView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null, false);
            draweeView = (SimpleDraweeView) view.findViewById(R.id.imgBanner);
            return view;
        }

        @Override
        public void UpdateUI(final Context context, int position, final Banner banner) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(banner.getUrl()))
                    .setProgressiveRenderingEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .build();
            draweeView.setController(controller);

            TextView detailTextView = new TextView(MainActivity.this);
            int padding = DensityUtil.dip2px(MainActivity.this, 16);
            detailTextView.setText(banner.getDescription());
            detailTextView.setPadding(padding, padding, padding, padding);
            detailTextView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            detailTextView.setTextSize(16);
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(detailTextView)
                    .setNegativeButton(
                            "查看详情", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this,
                                            banner.getDescription(), Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                    ).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();

            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                }
            });
        }

    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(3000);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_change_theme) {
            final ThemePickerFragment themePickerFragment = new ThemePickerFragment();

            themePickerFragment.setOnItemClickListener(new ThemePickerFragment.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    ThemeModel.getInstance().setStyle(position);
                    recreate();
                    themePickerFragment.dismiss();
                }
            });

            themePickerFragment.show(getSupportFragmentManager(),
                    ThemePickerFragment.DIALOG_THEME_PICKER);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else if (id == R.id.nav_change_semester) {
            LinkagePicker picker = SelectSemesterBuiler.getSelectSemesterPicker(this);
            picker.show();
            picker.setOnLinkageListener(new LinkagePicker.OnLinkageListener() {
                @Override
                public void onPicked(String first, String second, String third) {
                    Semester semester = new Semester(first, second);
                    User.getInstance().setCurrentSemester(semester);
                    setSemesterText();
                }
            });

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setHeadImage() {
        if (App.isDebug) return;
        headImageDraweeView.setImageURI(User.getInstance().getUserInfo().getAvatar());
    }

    private void setSemesterText() {
        if (App.isDebug) return;
        Semester semester = User.getInstance().getCurrentSemester();
        semesterTextView.setText(semester.getYearString() + " " + semester.getSeasonString());
    }

    private void setNiceName() {
        if (App.isDebug) return;
        nicknameTextView.setText(User.getInstance().getUserInfo().getNickname());
    }

    private void setBannerPage(List<Banner> banners) {
        if (banners.size() == 0) {
            return;
        }
        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, banners)
                .setPageIndicator(
                        new int[]{R.drawable.ic_page_indicator,
                                R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        mConvenientBanner.setcurrentitem(
                (int) (Math.random() * banners.size())
        );
    }

    private void getBanner() {
        Retrofit retrofit = RetrofitUtil.getDefault();
        BannerService bannerService = retrofit.create(BannerService.class);
        Log.d(TAG, "getBanner: ");
        bannerService.getBanner()
                .subscribeOn(Schedulers.io())
                .filter(new Func1<HttpResult<BannerInfo>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<BannerInfo> result) {
                        BannerInfo bannerInfo = result.getData();
                        if (bannerInfo.getLatest().getTimestamp() == mBannerModel.getTimestamp()) {
                            return false;
                        } else {
                            mBannerModel.setTimestamp(bannerInfo.getLatest().getTimestamp());
                            return true;
                        }
                    }
                })
                .filter(new Func1<HttpResult<BannerInfo>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<BannerInfo> result) {
                        if (RetrofitUtil.isSuccessful(result)) {
                            return true;
                        }
                        return false;
                    }
                }).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<BannerInfo>>() {
                    @Override
                    public void onCompleted() {
                        setBannerPage(mBannerModel.mBanners);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onNext(HttpResult<BannerInfo> result) {
                        BannerInfo bannerInfo = result.getData();
                        mBannerModel.setBanners(bannerInfo.getLatest().getBanners());
                    }
                });
    }
}
