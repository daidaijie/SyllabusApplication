package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.event.DeletePhotoEvent;
import com.example.daidaijie.syllabusapplication.event.ToTopEvent;
import com.example.daidaijie.syllabusapplication.other.PhotoDetailActivity;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.widget.FlowLabelLayout;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;
import com.example.daidaijie.syllabusapplication.widget.MaterialCheckBox;
import com.example.daidaijie.syllabusapplication.widget.MaxLinesTextWatcher;
import com.example.daidaijie.syllabusapplication.widget.picker.DateTimePicker;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.example.daidaijie.syllabusapplication.widget.picker.DateTimePicker.HOUR_OF_DAY;

public class PostDymaticActivity extends BaseActivity implements PostDymaticContract.view {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentEditText)
    EditText mContentEditText;
    @BindView(R.id.postImgFlowLayout)
    FlowLabelLayout mPostImgFlowLayout;
    @BindView(R.id.isHaveTimeCheckBox)
    MaterialCheckBox mIsHaveTimeCheckBox;
    @BindView(R.id.selectStartDateButton)
    TextView mSelectStartDateButton;
    @BindView(R.id.selectEndDateButton)
    TextView mSelectEndDateButton;
    @BindView(R.id.postNameEditText)
    EditText mPostNameEditText;
    @BindView(R.id.postPlaceEditText)
    EditText mPostPlaceEditText;
    @BindView(R.id.timeSelectLayout)
    LinearLayout mTimeSelectLayout;
    @BindView(R.id.postUrlEditText)
    EditText mPostUrlEditText;

    AlertDialog mLoadingDialog;

    public static final int MAX_IMG_NUM = 3;

    @Inject
    PostDymaticPresenter mPostDymaticPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setupTitleBar(mToolbar);

        if (mIsHaveTimeCheckBox.isChecked()) {
            mTimeSelectLayout.setVisibility(View.VISIBLE);
        } else {
            mTimeSelectLayout.setVisibility(View.GONE);
        }

        mIsHaveTimeCheckBox.setOnCheckedChangedListener(new MaterialCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean isChecked) {
                if (isChecked) {
                    mTimeSelectLayout.setVisibility(View.VISIBLE);
                } else {
                    mTimeSelectLayout.setVisibility(View.GONE);
                }
            }
        });

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeUtil.getInstance().colorPrimary);

        mSelectStartDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostDymaticPresenter.selectTime(true);
            }
        });
        mSelectEndDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPostDymaticPresenter.selectTime(false);
            }
        });

        mContentEditText.addTextChangedListener(new MaxLinesTextWatcher(mContentEditText, 16));

        DaggerPostDymaticComponent.builder()
                .schoolDymaticModelComponent(SchoolDymaticModelComponent.getINSTANCE(mAppComponent))
                .postDymaticModule(new PostDymaticModule(this))
                .build().inject(this);

        mPostDymaticPresenter.start();

    }

    @Override
    public void selectTime(final boolean isStart, int year, int month, int day, int hour, int minute) {
        DateTimePicker dateTimePicker = new DateTimePicker(this, HOUR_OF_DAY);
        dateTimePicker.setSelectedItem(year, month, day, hour, minute);
        Logger.t("PostActivity").e(String.valueOf(minute));

        dateTimePicker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                mPostDymaticPresenter.setTime(
                        isStart,
                        Integer.parseInt(year),
                        Integer.parseInt(month),
                        Integer.parseInt(day),
                        Integer.parseInt(hour),
                        Integer.parseInt(minute));
            }
        });
        dateTimePicker.show();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_post_dymatic;
    }

    @Override
    public void showLoading(boolean isShow) {
        if (isShow) {
            mLoadingDialog.show();
        } else {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void onPostFinishCallBack() {
        EventBus.getDefault().post(new ToTopEvent(true, "发送成功"));
        this.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deletePhoto(DeletePhotoEvent event) {
        mPostDymaticPresenter.unSelectPhoto(event.position);
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
                    Intent intent = PhotoDetailActivity.getIntent(PostDymaticActivity.this,
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
                    "res://" + PostDymaticActivity.this.getPackageName()
                            + "/" + R.drawable.ic_action_add_post
            ));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPostDymaticPresenter.selectPhoto();
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
        SnackbarUtil.ShortSnackbar(mContentEditText, msg, SnackbarUtil.Warning).show();
    }

    @Override
    public void setStartTimeString(String timeString) {
        mSelectStartDateButton.setText(timeString);
    }

    @Override
    public void setEndTimeString(String timeString) {
        mSelectEndDateButton.setText(timeString);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            hideInput(mPostNameEditText);
            hideInput(mPostPlaceEditText);

            mPostDymaticPresenter.postContent(
                    mContentEditText.getText().toString().trim(),
                    mPostNameEditText.getText().toString().trim(),
                    mPostUrlEditText.getText().toString().trim(),
                    mPostPlaceEditText.getText().toString().trim(),
                    mIsHaveTimeCheckBox.isChecked());

        }
        return super.onOptionsItemSelected(item);
    }
}
