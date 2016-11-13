package com.example.daidaijie.syllabusapplication.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.activity.EmailWebActivity;
import com.example.daidaijie.syllabusapplication.activity.LoginInternetActivity;
import com.example.daidaijie.syllabusapplication.dialog.ThemePickerFragment;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.Banner;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.bean.StreamInfo;
import com.example.daidaijie.syllabusapplication.bean.UserInfo;
import com.example.daidaijie.syllabusapplication.event.InternetOpenEvent;
import com.example.daidaijie.syllabusapplication.event.UpdateUserInfoEvent;
import com.example.daidaijie.syllabusapplication.exam.mainMenu.ExamActivity;
import com.example.daidaijie.syllabusapplication.grade.GradeActivity;
import com.example.daidaijie.syllabusapplication.login.login.LoginActivity;
import com.example.daidaijie.syllabusapplication.model.InternetModel;
import com.example.daidaijie.syllabusapplication.other.update.IDownloadView;
import com.example.daidaijie.syllabusapplication.other.update.UpdateInstaller;
import com.example.daidaijie.syllabusapplication.retrofitApi.SchoolInternetApi;
import com.example.daidaijie.syllabusapplication.services.StreamService;
import com.example.daidaijie.syllabusapplication.stream.IStreamModel;
import com.example.daidaijie.syllabusapplication.stream.StreamModel;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.ShareWXUtil;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu.OfficeAutomationActivity;
import com.example.daidaijie.syllabusapplication.other.AboutUsActivity;
import com.example.daidaijie.syllabusapplication.other.CommonWebActivity;
import com.example.daidaijie.syllabusapplication.other.PhotoDetailActivity;
import com.example.daidaijie.syllabusapplication.other.update.UpdateActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.STUCircleActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.personal.PersonalActivity;
import com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu.LibraryActivity;
import com.example.daidaijie.syllabusapplication.syllabus.main.activity.SyllabusActivity;
import com.example.daidaijie.syllabusapplication.takeout.mainMenu.TakeOutActivity;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.util.UpdateAsync;
import com.example.daidaijie.syllabusapplication.widget.ItemCardLayout;
import com.example.daidaijie.syllabusapplication.widget.SelectSemesterBuilder;
import com.example.daidaijie.syllabusapplication.widget.ShareWXDialog;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements
        MainContract.view, NavigationView.OnNavigationItemSelectedListener, ShareWXDialog.OnShareSelectCallBack, IDownloadView, UpdateInstaller {

    @BindView(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;

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
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.toWifiItemLayout)
    ItemCardLayout mToWifiItemLayout;
    @BindView(R.id.toLibraryCardItem)
    ItemCardLayout mToLibraryCardItem;
    @BindView(R.id.toTakeOutCardItem)
    ItemCardLayout mToTakeOutCardItem;

    RelativeLayout navHeadRelativeLayout;
    SimpleDraweeView headImageDraweeView;
    TextView semesterTextView;
    TextView nicknameTextView;

    @Inject
    MainPresenter mMainPresenter;

    LinkagePicker picker;

    Timer mTimer;

    IStreamModel streamModel;

    boolean isActive;

    // 用于显示下载进度
    private ProgressDialog progressDialog;
    private UpdateAsync updateAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        /**
         * 设置Banner的高,使其符合16:9
         */
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

        setOnItemClick();

        DaggerMainComponent.builder()
                .userComponent(UserComponent.buildInstance(mAppComponent))
                .mainModule(new MainModule(this, this))
                .build().inject(this);

        mMainPresenter.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://1.1.1.2/ac_portal/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        final SchoolInternetApi schoolInternetApi = retrofit.create(SchoolInternetApi.class);
        streamModel = new StreamModel(schoolInternetApi);

        if (InternetModel.getInstance().isOpen()) {
            startStreamListen();
        } else {
            stopStreamListen();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void showProgress(int done, int total) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("下载进度");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
//            showInfoMessage("已下载大小" + done + " 总大小" + total);
            progressDialog.setMax(total);
            // 暂时不考虑这个功能了
//            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    showInfoMessage("取消下载");
//                }
//            });
            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        progressDialog.show();
        progressDialog.setProgress(done);

    }

    @Override
    public void installUpdate(File apk) {
        progressDialog.dismiss();
        if (apk != null && apk.exists()) {
            showSuccessMessage("文件下载成功");
            // 隐式的 intent
            Intent install_apk = new Intent(Intent.ACTION_VIEW);
            // 安装 apk 文件
            install_apk.setDataAndType(Uri.parse("file://" + apk.toString()), "application/vnd.android.package-archive");
            startActivity(install_apk);
        } else
            showFailMessage("文件下载失败");
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
                                    Intent intent = null;
                                    if (banner.getLink().equals(banner.getUrl())) {
                                        List urls = new ArrayList();
                                        urls.add(banner.getLink());
                                        intent = PhotoDetailActivity.getIntent(MainActivity.this, urls, 0);
                                    } else {
                                        intent = CommonWebActivity.getIntent(MainActivity.this, banner.getLink(), banner.getDescription());
                                    }
                                    startActivity(intent);
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
                public void onClick(String name) {
                    ThemeUtil.getInstance().setStyle(name);
                    recreate();
                    themePickerFragment.dismiss();
                }
            });

            themePickerFragment.show(getSupportFragmentManager(),
                    ThemePickerFragment.DIALOG_THEME_PICKER);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            UserComponent.destory();
            this.finish();
        } else if (id == R.id.nav_change_semester) {
            mMainPresenter.showSemesterSelect();
        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share_app) {
            showShareDialog();
        } else if (id == R.id.nav_personal_info) {
            Intent intent = new Intent(this, PersonalActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_show_update) {
            Intent intent = new Intent(this, UpdateActivity.class);
            startActivity(intent);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setBannerPage(List<Banner> banners) {
        if (banners == null || banners.size() == 0) {
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

    @Override
    public void showUserInfo(UserInfo mUserInfo) {
        nicknameTextView.setText(mUserInfo.getNickname());
        headImageDraweeView.setImageURI(mUserInfo.getAvatar());
    }

    @Override
    public void showSemester(Semester semester) {
        semesterTextView.setText(semester.getYearString() + " " + semester.getSeasonString());
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mCoordinatorLayout, msg, SnackbarUtil.Info).show();
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mCoordinatorLayout, msg, SnackbarUtil.Alert).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mCoordinatorLayout, msg, SnackbarUtil.Confirm).show();
    }

    @Override
    public void setCurrentSemester(Semester semester) {
        picker = SelectSemesterBuilder.newSelectSemesterPicker(this, semester);
        picker.show();
        picker.setOnLinkageListener(new LinkagePicker.OnLinkageListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                mMainPresenter.setCurrentSemester(new Semester(first, second));
            }
        });
    }

    @Override
    public void showUpdateInfo(String updateInfo, final OnUpdateClickCallBack onUpdateClickCallBack) {
        AlertDialog updateDialog = new AlertDialog.Builder(this)
                .setTitle("更新详情")
                .setMessage(updateInfo)
                .setPositiveButton("取消", null)
                .setNegativeButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onUpdateClickCallBack.onUpdate();
                    }
                }).create();
        updateDialog.show();
    }

    private void setOnItemClick() {
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
    }

    private void share(int scene) {
        ShareWXUtil.shareUrl("http://fir.im/syllabus", "汕大课程表", "汕大课程表下载地址",
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_syllabus_icon), scene
        );
    }


    private void showShareDialog() {
        ShareWXDialog dialog = new ShareWXDialog();
        dialog.setOnShareSelectCallBack(this);
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void onShareSelect(int position) {
        if (position == 0) {
            share(0);
        } else if (position == 1) {
            share(1);
        } else {
            ClipboardUtil.copyToClipboard("http://fir.im/syllabus");
            showInfoMessage("已复制下载链接到剪贴板");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStream(InternetOpenEvent event) {
        if (event.isOpen()) {
            startStreamListen();
        } else {
            stopStreamListen();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateInfo(UpdateUserInfoEvent event) {
        mMainPresenter.showUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopStreamListen();
    }

    private void startStreamListen() {
        isActive = true;
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                streamModel.getStreamInfo()
                        .subscribe(new Action1<StreamInfo>() {
                            @Override
                            public void call(StreamInfo streamInfo) {
                                if (InternetModel.getInstance().isOpen() && isActive) {
                                    Intent intent = StreamService.getIntent(MainActivity.this);
                                    startService(intent);
                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                if (InternetModel.getInstance().isOpen() && isActive) {
                                    Intent intent = StreamService.getIntent(MainActivity.this);
                                    startService(intent);
                                }
                            }
                        });
            }
        }, 0, 3000);
    }

    private void stopStreamListen() {
        isActive = false;
        if (mTimer != null) {
            mTimer.cancel();
            Intent intent = StreamService.getIntent(MainActivity.this);
            stopService(intent);
        }
    }
}
