package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.PhotoDetailAdapter;
import com.example.daidaijie.syllabusapplication.bean.PhotoInfo;
import com.example.daidaijie.syllabusapplication.event.DeletePhotoEvent;
import com.example.daidaijie.syllabusapplication.widget.MultiTouchViewPager;
import com.liaoinstan.springview.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;

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
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_PHOTO_Mode, 0);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMode == 0) return true;
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_crash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_crash) {
            TextView textView = new TextView(this);
            textView.setTextSize(16);
            int padding = DensityUtil.dip2px(this, 16);
            textView.setPadding(padding + padding / 2, padding, padding, padding);
            textView.setText("要删除这张图片吗?");
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
                            EventBus.getDefault().post(new DeletePhotoEvent(mPosition));
                            mPhotoUrls.remove(mPosition);
                            mPhotoDetailAdapter.setPhotoInfo(mPhotoUrls);
                            mPhotoDetailAdapter.notifyDataSetChanged();

                            PhotoDetailActivity.this.finish();

                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

}
