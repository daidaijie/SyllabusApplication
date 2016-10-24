package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.CollectionBean;
import com.example.daidaijie.syllabusapplication.bean.CollectionId;
import com.example.daidaijie.syllabusapplication.bean.HttpResult;
import com.example.daidaijie.syllabusapplication.bean.Semester;
import com.example.daidaijie.syllabusapplication.event.CollectionStateChangeEvent;
import com.example.daidaijie.syllabusapplication.model.SyllabusCollectionModel;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.retrofitApi.SyllabusCollectionService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;
import com.example.daidaijie.syllabusapplication.widget.SelectSemesterBuilder;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import info.hoang8f.widget.FButton;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCollectionActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.finishButton)
    FButton mFinishButton;
    @BindView(R.id.selectSemesterButton)
    FButton mSelectSemesterButton;
    LinkagePicker mLinkagePicker;
    AlertDialog mLoadingDialog;

    Semester mSelectSemester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeUtil.getInstance().colorPrimary);
        mLinkagePicker = SelectSemesterBuilder.getSelectSemesterPicker(this);
        mSelectSemester = User.getInstance().getCurrentSemester();
        setSelectButtonText();

        mLinkagePicker.setOnLinkageListener(new LinkagePicker.OnLinkageListener() {
            @Override
            public void onPicked(String first, String second, String third) {
                mSelectSemester = new Semester(first, second);
                setSelectButtonText();
            }
        });

        mSelectSemesterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinkagePicker.show();
            }
        });

        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        mLoadingDialog.show();
        SyllabusCollectionService collectionService = RetrofitUtil.getDefault()
                .create(SyllabusCollectionService.class);
        collectionService.addCollection(User.getInstance().getCurrentAccount(),
                User.getInstance().getUserInfo().getToken(),
                mSelectSemester.getStartYear(),
                mSelectSemester.getSeason())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResult<CollectionId>>() {
                    @Override
                    public void onCompleted() {
                        mLoadingDialog.dismiss();
                        EventBus.getDefault().post(new CollectionStateChangeEvent());
                        AddCollectionActivity.this.finish();
                        Log.e("AddCollectionActivity", "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mLoadingDialog.dismiss();
                        showFailBanner("申请失败");
                    }

                    @Override
                    public void onNext(HttpResult<CollectionId> collectionIdHttpResult) {
                        if (RetrofitUtil.isSuccessful(collectionIdHttpResult)) {
                            CollectionBean collectionBean = new CollectionBean();
                            collectionBean.setSeason(mSelectSemester.getSeason());
                            collectionBean.setStart_year(mSelectSemester.getStartYear());
                            collectionBean.setCollection_id(collectionIdHttpResult.getData().getCollection_id());
                            SyllabusCollectionModel.getInstance().getCollectionInfo().getCollection_ids()
                                    .add(collectionBean);
                        } else {
                            showFailBanner(collectionIdHttpResult.getMessage());
                        }
                        Log.e("AddCollectionActivity", "onNext: ");
                    }
                });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_add_collection;
    }

    private void setSelectButtonText() {
        mSelectSemesterButton.setText(mSelectSemester.getYearString()
                + " " + mSelectSemester.getSeasonString());
    }

    public void showFailBanner(String msg) {
        SnackbarUtil.LongSnackbar(
                mFinishButton,
                msg.toUpperCase(),
                SnackbarUtil.Alert
        );
    }

}
