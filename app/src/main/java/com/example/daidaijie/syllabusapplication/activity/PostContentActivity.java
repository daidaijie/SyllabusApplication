package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.BmobPhoto;
import com.example.daidaijie.syllabusapplication.bean.PostContent;
import com.example.daidaijie.syllabusapplication.event.DeletePhotoEvent;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.retrofitApi.PushPostService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.ImageUploader;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.FlowLabelLayout;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;
import com.facebook.drawee.view.SimpleDraweeView;
import com.liaoinstan.springview.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class PostContentActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private final int MAX_IMG_NUM = 3;
    @BindView(R.id.postImgFlowLayout)
    FlowLabelLayout mPostImgFlowLayout;
    @BindView(R.id.contentEditText)
    EditText mContentEditText;
    @BindView(R.id.postAsAndroidButton)
    AppCompatRadioButton mPostAsAndroidButton;
    @BindView(R.id.postAsPhoneButton)
    AppCompatRadioButton mPostAsPhoneButton;
    @BindView(R.id.postAsOtherButton)
    AppCompatRadioButton mPostAsOtherButton;

    private List<String> mPhotoImgs;

    public static final String TAG = "PostContentActivity";

    AlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPostAsPhoneButton.setText(Build.MODEL);

        mPhotoImgs = new ArrayList<>();
        setUpFlow();
        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeModel.getInstance().colorPrimary);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_post_content;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setUpFlow() {
        mPostImgFlowLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        lp.leftMargin = 0;
        lp.rightMargin = 32;
        lp.topMargin = 0;
        lp.bottomMargin = 0;
        for (int i = 0; i < mPhotoImgs.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_edit_img, null, false);
            SimpleDraweeView imgDraweeView = (SimpleDraweeView) view.findViewById(R.id.imgDraweeView);
            imgDraweeView.setImageURI(Uri.parse(mPhotoImgs.get(i)));
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PhotoDetailActivity.getIntent(PostContentActivity.this,
                            mPhotoImgs, finalI, 1);
                    startActivity(intent);
                }
            });

            mPostImgFlowLayout.addView(view, lp);

        }
        if (mPhotoImgs.size() < MAX_IMG_NUM) {
            View view = getLayoutInflater().inflate(R.layout.item_edit_img, null, false);
            SimpleDraweeView imgDraweeView = (SimpleDraweeView) view.findViewById(R.id.imgDraweeView);
            imgDraweeView.setImageURI(Uri.parse(
                    "res://" + PostContentActivity.this.getPackageName()
                            + "/" + R.drawable.ic_action_add_post
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seletePhoto();
                }
            });
            mPostImgFlowLayout.addView(view, lp);
        }
    }

    private void seletePhoto() {
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setMutiSelectMaxSize(MAX_IMG_NUM - mPhotoImgs.size())
                .setEnableCamera(false)
                .setEnableEdit(false)
                .setEnableCrop(false)
                .setEnableRotate(true)
                .setEnablePreview(false)
                .setCropReplaceSource(false)//配置裁剪图片时是否替换原始图片，默认不替换
                .setForceCrop(false)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .build();

        GalleryFinal.openGalleryMuti(200, functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                for (PhotoInfo photoInfo : resultList) {
                    mPhotoImgs.add("file://" + photoInfo.getPhotoPath());
                    Log.d(TAG, "onHanlderSuccess: " + photoInfo.getPhotoPath());
                    setUpFlow();
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_finish) {

            if (mContentEditText.getText().toString().trim().isEmpty() && mPhotoImgs.size() == 0) {
                SnackbarUtil.ShortSnackbar(
                        mContentEditText, "请输入文字或添加图片", SnackbarUtil.Warning
                ).show();
            }

            if (mPhotoImgs.size() != 0) {
                final MediaType mediaType = MediaType.parse("image/*");
                Observable.from(mPhotoImgs)
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<String, File>() {
                            @Override
                            public File call(String s) {
                                return new File(s.substring("file://".length(), s.length()));
                            }
                        })
                        .flatMap(new Func1<File, Observable<File>>() {
                            @Override
                            public Observable<File> call(File file) {
//                            Log.d(TAG, "call: " + file.exists());
                                Log.d(TAG, "压缩前: " + Formatter.formatFileSize(PostContentActivity.this, file.length()));
                                return Compressor.getDefault(PostContentActivity.this)
                                        .compressToFileAsObservable(file);
                            }
                        })
                        .flatMap(new Func1<File, Observable<BmobPhoto>>() {
                            @Override
                            public Observable<BmobPhoto> call(File file) {
                                Log.d(TAG, "压缩后: " + Formatter.formatFileSize(PostContentActivity.this, file.length()));
                                return ImageUploader.getObservableAsBombPhoto(mediaType,
                                        file.toString(), file);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BmobPhoto>() {
                            com.example.daidaijie.syllabusapplication.bean.PhotoInfo photoInfo =
                                    new com.example.daidaijie.syllabusapplication.bean.PhotoInfo();

                            @Override
                            public void onStart() {
                                super.onStart();
                                photoInfo.setPhoto_list(new ArrayList<com.example.daidaijie
                                        .syllabusapplication.bean.PhotoInfo.PhotoListBean>());
                            }

                            @Override
                            public void onCompleted() {
                                String photoListJsonString = GsonUtil.getDefault()
                                        .toJson(photoInfo, com.example.daidaijie
                                                .syllabusapplication.bean.PhotoInfo.class);
                                Log.d(TAG, "onCompleted: " + photoListJsonString);
                                pushContent(photoListJsonString);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: " + e.getMessage());
                                Toast.makeText(PostContentActivity.this,
                                        "图片上传失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(BmobPhoto bmobPhoto) {
                                Log.d(TAG, "onNext: " + bmobPhoto.getUrl());
                                com.example.daidaijie.syllabusapplication.bean.PhotoInfo
                                        .PhotoListBean photoListBean = new com.example.daidaijie
                                        .syllabusapplication.bean.PhotoInfo.PhotoListBean();
                                photoListBean.setSize_big(bmobPhoto.getUrl());
                                photoListBean.setSize_small(bmobPhoto.getUrl());
                                photoInfo.getPhoto_list().add(photoListBean);
                            }
                        });
            } else {
                pushContent(null);
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void pushContent(@Nullable String photoListJson) {
        mLoadingDialog.show();
        PostContent postContent = new PostContent();
        postContent.content = mContentEditText.getText().toString();
        postContent.token = User.getInstance().getUserInfo().getToken();
        postContent.description = "None";
        postContent.post_type = PushPostService.POST_TYPE_TOPIC;
        postContent.photo_list_json = photoListJson;
        if (mPostAsAndroidButton.isChecked()) {
            postContent.source = mPostAsAndroidButton.getText().toString();
        } else if (mPostAsPhoneButton.isChecked()) {
            postContent.source = mPostAsPhoneButton.getText().toString();
        } else if (mPostAsOtherButton.isChecked()) {
            postContent.source = mPostAsOtherButton.getText().toString();
        }
        postContent.uid = User.getInstance().getUserBaseBean().getId();

        Retrofit retrofit = RetrofitUtil.getDefault();
        PushPostService pushPostService = retrofit.create(PushPostService.class);
        pushPostService.post(postContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        EventBus.getDefault().post(new ToTopEvent(true, true));
                        mLoadingDialog.dismiss();
                        PostContentActivity.this.finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: Push" + e.getMessage());
                        SnackbarUtil.LongSnackbar(mContentEditText, "发送失败", SnackbarUtil.Alert)
                                .show();
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (mPhotoImgs.size() != 0 || !mContentEditText.getText().toString().trim().isEmpty()) {
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            textView.setTextColor(getResources().getColor(R.color.defaultTextColor));
            int padding = DensityUtil.dip2px(this, 16);
            textView.setPadding(padding + padding / 2, padding, padding, padding);
            textView.setText("你正在编辑中,是否要退出?");
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setView(textView)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PostContentActivity.this.finish();
                        }
                    }).create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, PostContentActivity.class);
        return intent;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deletePhoto(DeletePhotoEvent event) {
        mPhotoImgs.remove(event.position);
        setUpFlow();
    }
}
