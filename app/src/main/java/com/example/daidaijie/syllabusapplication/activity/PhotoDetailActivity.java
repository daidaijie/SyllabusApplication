package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.PhotoDetailAdapter;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.widget.MultiTouchViewPager;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;

public class PhotoDetailActivity extends BaseActivity {

    public static final String EXTRA_PHOTO_INFO
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoList";

    public static final String EXTRA_PHOTO_POSITION
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoPosition";

    public static final String EXTRA_PHOTO_Mode
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoMode";


    @BindView(R.id.photoViewpager)
    MultiTouchViewPager mPhotoViewpager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;

    private PhotoDetailAdapter mPhotoDetailAdapter;

    private int mPosition;

    private List<String> mPhotoUrls;

    private int mPhotoCount;

    /**
     * 0 普通
     * 1 删除模式
     */
    private int mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_PHOTO_Mode, 0);

        mPhotoUrls = (List<String>) intent.getSerializableExtra(EXTRA_PHOTO_INFO);
        mPosition = intent.getIntExtra(EXTRA_PHOTO_POSITION, 0);
        mPhotoCount = mPhotoUrls.size();

        mPhotoDetailAdapter = new PhotoDetailAdapter(this, mPhotoUrls);
        mPhotoViewpager.setAdapter(mPhotoDetailAdapter);
        mPhotoViewpager.setCurrentItem(mPosition);
        pointTitle(mPosition);
        mPhotoViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                pointTitle(position);
            }
        });

    }

    private void pointTitle(int position) {
        mTitleTextView.setText("查看大图 (" + (position + 1) + "/" + mPhotoCount + ")");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photo_detail;
    }

    public static Intent getIntent(Context context, List<String> urls, int position) {
        return getIntent(context, urls, position, 0);
    }

    public static Intent getIntent(Context context, List<String> urls, int position, int mode) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(EXTRA_PHOTO_INFO, (Serializable) urls);
        intent.putExtra(EXTRA_PHOTO_POSITION, position);
        intent.putExtra(EXTRA_PHOTO_Mode, mode);
        return intent;
    }
}
