package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.OAPagerAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.OASearchBean;
import com.example.daidaijie.syllabusapplication.event.OAClearEvent;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAModelComponent;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAUtil;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;

public class OfficeAutomationActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.contentViewPager)
    ViewPager mContentViewPager;

    OAPagerAdapter mOAPagerAdapter;

    AlertDialog mSearchDialog;
    AlertDialog mSubDialog;

    Button selectSubButton;
    EditText keywordEditText;

    @BindView(R.id.showPageSelectImageView)
    ImageView mShowPageSelectImageView;
    @BindView(R.id.pageTitleLayout)
    LinearLayout mPageTitleLayout;
    @BindView(R.id.searchButton)
    FloatingActionButton mSearchButton;
    @BindView(R.id.titleLayout)
    LinearLayout mTitleLayout;
    @BindView(R.id.pageEditText)
    EditText mPageEditText;
    @BindView(R.id.gotoPageButton)
    FButton mGotoPageButton;

    OASearchBean mOASearchBean;

    OAUtil mOAUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        setupTitleBar(mToolbar);

        /**
         * 初始化
         */
        mOAUtil = new OAUtil();
        mOASearchBean = new OASearchBean();
        OAModelComponent.destory();
        OAModelComponent.newInstance(UserComponent.buildInstance(mAppComponent), mOASearchBean);
        mOAPagerAdapter = new OAPagerAdapter(getSupportFragmentManager());
        mContentViewPager.setAdapter(mOAPagerAdapter);
        mContentViewPager.setCurrentItem(0);


        mContentViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTitleTextView.setText("第 " + (position + 1) + " 页");
                animateIn(mSearchButton);
            }
        });
        mTitleTextView.setText("第 " + (mContentViewPager.getCurrentItem() + 1) + " 页");


        mSubDialog = new AlertDialog.Builder(this)
                .setTitle("选择部门")
                .setItems(mOAUtil.getSubCompanysString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectSubButton.setText(
                                mOAUtil.mSubCompanies.get(which).getSUBCOMPANYNAME());
                        mOASearchBean.setSubcompanyId(mOAUtil.mSubCompanies.get(which).getID());
                    }
                })
                .create();

        View view = getLayoutInflater().inflate(R.layout.dialog_search_oa, null, false);
        selectSubButton = (Button) view.findViewById(R.id.subButtonCard);
        selectSubButton.setText(mOAUtil.mSubCompanies.get(mOASearchBean.getSubcompanyId()).getSUBCOMPANYNAME());
        selectSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubDialog.show();
            }
        });
        keywordEditText = (EditText) view.findViewById(R.id.keywordEditText);

        mSearchDialog = new AlertDialog.Builder(this)
                .setTitle("搜索")
                .setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keywordEditText.setText(mOASearchBean.getKeyword());
                    }
                }).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mOASearchBean.setKeyword(keywordEditText.getText().toString().trim());
                        OAModelComponent.destory();
                        OAModelComponent.newInstance(UserComponent.buildInstance(mAppComponent), mOASearchBean);
                        mOAPagerAdapter = new OAPagerAdapter(getSupportFragmentManager());
                        mContentViewPager.setAdapter(mOAPagerAdapter);
                        mContentViewPager.setCurrentItem(0);
                        mTitleTextView.setText("第 " + (mContentViewPager.getCurrentItem() + 1) + " 页");
                    }
                }).create();

        showPage(false);
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageTitleLayout.getVisibility() == View.GONE) {
                    showPage(true);
                } else {
                    showPage(false);
                }
            }
        });

        mGotoPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPageEditText.getText().toString().isEmpty()) {
                    mPageEditText.setError("输入不能为空");
                    return;
                }
                mContentViewPager.setCurrentItem(Integer.parseInt(mPageEditText.getText().toString()) - 1);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_office_automation;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oa_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_browse) {
            AlertDialog deleteEnsureDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定删除办公自动化浏览记录?")
                    .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OAModelComponent.getINSTANCE().getOAModel().clearRead();
                            EventBus.getDefault().post(new OAClearEvent());
                        }
                    })
                    .create();

            deleteEnsureDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.searchButton)
    public void onClick() {
        mSearchDialog.show();
    }

    private void showPage(boolean isShow) {
        if (isShow) {
            mPageEditText.setText("");
            mPageTitleLayout.setVisibility(View.VISIBLE);
            mShowPageSelectImageView.setRotation(180.0f);
        } else {
            mPageTitleLayout.setVisibility(View.GONE);
            mShowPageSelectImageView.setRotation(0.0f);
        }
    }

    private void animateIn(FloatingActionButton button) {
        button.setVisibility(View.VISIBLE);
        ViewCompat.animate(button).translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator()).withLayer().setListener(null)
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OAModelComponent.destory();
    }
}
