package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.PhotoAdapter;
import com.example.daidaijie.syllabusapplication.adapter.PhotoDetailAdapter;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PHOTO_INFO
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoList";

    public static final String EXTRA_PHOTO_POSITION
            = "com.example.daidaijie.syllabusapplication.activity.PhotoInfoPosition";

    @BindView(R.id.photoViewpager)
    ViewPager mPhotoViewpager;

    private PhotoDetailAdapter mPhotoDetailAdapter;

    private int mPosition;

    private PhotoInfo mPhotoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_photo_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        mPhotoInfo = (PhotoInfo) intent.getSerializableExtra(EXTRA_PHOTO_INFO);
        mPosition = intent.getIntExtra(EXTRA_PHOTO_POSITION, 0);

        mPhotoDetailAdapter = new PhotoDetailAdapter(this, mPhotoInfo);
        mPhotoViewpager.setAdapter(mPhotoDetailAdapter);
        mPhotoViewpager.setCurrentItem(mPosition);

    }

    public static Intent getIntent(Context context, PhotoInfo photoInfo, int position) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(EXTRA_PHOTO_INFO, photoInfo);
        intent.putExtra(EXTRA_PHOTO_POSITION, position);
        return intent;
    }

}
