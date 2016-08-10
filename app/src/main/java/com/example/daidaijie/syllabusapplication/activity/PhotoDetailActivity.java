package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.PhotoDetailAdapter;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.widget.MultiTouchViewPager;

import butterknife.BindView;

public class PhotoDetailActivity extends BaseActivity {

    public static final String EXTRA_PHOTO_INFO
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoList";

    public static final String EXTRA_PHOTO_POSITION
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoPosition";

    @BindView(R.id.photoViewpager)
    MultiTouchViewPager mPhotoViewpager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;

    private PhotoDetailAdapter mPhotoDetailAdapter;

    private int mPosition;

    private PhotoInfo mPhotoInfo;

    private int mPhotoCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        mPhotoInfo = (PhotoInfo) intent.getSerializableExtra(EXTRA_PHOTO_INFO);
        mPosition = intent.getIntExtra(EXTRA_PHOTO_POSITION, 0);
        mPhotoCount = mPhotoInfo.getPhoto_list().size();

        mPhotoDetailAdapter = new PhotoDetailAdapter(this, mPhotoInfo);
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

    public static Intent getIntent(Context context, PhotoInfo photoInfo, int position) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(EXTRA_PHOTO_INFO, photoInfo);
        intent.putExtra(EXTRA_PHOTO_POSITION, position);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
