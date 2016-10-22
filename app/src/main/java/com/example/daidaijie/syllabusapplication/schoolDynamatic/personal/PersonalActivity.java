package com.example.daidaijie.syllabusapplication.schoolDynamatic.personal;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.UserBaseBean;
import com.example.daidaijie.syllabusapplication.main.MainPresenter;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.facebook.drawee.view.SimpleDraweeView;

import javax.inject.Inject;

import butterknife.BindView;
import info.hoang8f.widget.FButton;

public class PersonalActivity extends BaseActivity implements PersonalContract.view {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.headImageDraweeView)
    SimpleDraweeView mHeadImageDraweeView;
    @BindView(R.id.accountEditText)
    EditText mAccountEditText;
    @BindView(R.id.niceNameEditText)
    EditText mNiceNameEditText;
    @BindView(R.id.sayEditText)
    EditText mSayEditText;
    @BindView(R.id.finishButton)
    FButton mFinishButton;

    @Inject
    PersonalPresenter mPersonalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        DaggerPersonalComponent.builder()
                .userComponent(UserComponent.getINSTANCE())
                .personalModule(new PersonalModule(this))
                .build().inject(this);

        mPersonalPresenter.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_personal;
    }

    @Override
    public void showUserBase(UserBaseBean userBaseBean) {
        mHeadImageDraweeView.setImageURI(userBaseBean.getPhoto());
        mNiceNameEditText.setText(userBaseBean.getNickname());
        mAccountEditText.setText(userBaseBean.getAccount());
        mSayEditText.setText(userBaseBean.getProfile());

    }
}
