package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.postContent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.event.DeletePhotoEvent;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;
import com.example.daidaijie.syllabusapplication.other.PhotoDetailActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.StuCircleModelComponent;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.widget.FlowLabelLayout;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;
import com.example.daidaijie.syllabusapplication.widget.MaxLinesTextWatcher;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PostContentActivity extends BaseActivity implements PostContentContract.view {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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

    AlertDialog mLoadingDialog;

    @Inject
    PostContentPresenter mPostContentPresenter;

    public static final int MAX_IMG_NUM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setupTitleBar(mToolbar);

        mPostAsPhoneButton.setText(Build.MODEL);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeUtil.getInstance().colorPrimary);

        DaggerPostContentComponent.builder()
                .stuCircleModelComponent(StuCircleModelComponent.getInstance(mAppComponent))
                .postContentModule(new PostContentModule(this))
                .build().inject(this);

        mPostContentPresenter.start();

        mContentEditText.addTextChangedListener(new MaxLinesTextWatcher(mContentEditText, 16));
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

    @Override
    public void setUpFlow(final List<String> PhotoImgs) {
        mPostImgFlowLayout.removeAllViews();

        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        lp.leftMargin = 0;
        lp.rightMargin = 32;
        lp.topMargin = 0;
        lp.bottomMargin = 0;
        for (int i = 0; i < PhotoImgs.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_edit_img, null, false);
            SimpleDraweeView imgDraweeView = (SimpleDraweeView) view.findViewById(R.id.imgDraweeView);
            imgDraweeView.setImageURI(Uri.parse(PhotoImgs.get(i)));
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = PhotoDetailActivity.getIntent(PostContentActivity.this,
                            PhotoImgs, finalI, 1);
                    startActivity(intent);
                }
            });

            mPostImgFlowLayout.addView(view, lp);

        }
        if (PhotoImgs.size() < MAX_IMG_NUM) {
            View view = getLayoutInflater().inflate(R.layout.item_edit_img, null, false);
            SimpleDraweeView imgDraweeView = (SimpleDraweeView) view.findViewById(R.id.imgDraweeView);
            imgDraweeView.setImageURI(Uri.parse(
                    "res://" + PostContentActivity.this.getPackageName()
                            + "/" + R.drawable.ic_action_add_post
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPostContentPresenter.selectPhoto();
                }
            });
            mPostImgFlowLayout.addView(view, lp);
        }
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mContentEditText, msg, SnackbarUtil.Alert).show();
    }

    @Override
    public void showWarningMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mContentEditText, msg, SnackbarUtil.Warning
        ).show();
    }

    @Override
    public void onPostFinishCallBack() {
        EventBus.getDefault().post(new ToTopEvent(true, "发送成功"));
        this.finish();
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

            hideInput(mContentEditText);

            String source;
            if (mPostAsAndroidButton.isChecked()) {
                source = mPostAsAndroidButton.getText().toString();
            } else if (mPostAsPhoneButton.isChecked()) {
                source = mPostAsPhoneButton.getText().toString();
            } else if (mPostAsOtherButton.isChecked()) {
                source = mPostAsOtherButton.getText().toString();
            } else {
                source = "火星";
            }

            mPostContentPresenter.postContent(mContentEditText.getText().toString().trim(), source);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        hideInput(mContentEditText);
        if (!mPostContentPresenter.isNonePhoto() || !mContentEditText.getText().toString().trim().isEmpty()) {
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
        mPostContentPresenter.unSelectPhoto(event.position);
    }

    @Override
    public void showLoading(boolean isShow) {
        if (isShow) {
            mLoadingDialog.show();
        } else {
            mLoadingDialog.dismiss();
        }
    }
}
