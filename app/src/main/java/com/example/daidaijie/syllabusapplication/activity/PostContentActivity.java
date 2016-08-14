package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.event.DeletePhotoEvent;
import com.example.daidaijie.syllabusapplication.widget.FlowLabelLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.liaoinstan.springview.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

    private List<String> mPhotoImgs;

    public static final String TAG = "PostContentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPhotoImgs = new ArrayList<>();
        setUpFlow();
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

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
                    setUpFlow();
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
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
