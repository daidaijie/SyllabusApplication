package com.example.daidaijie.syllabusapplication.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
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
import com.example.daidaijie.syllabusapplication.bean.OARead;
import com.example.daidaijie.syllabusapplication.event.OAClearEvent;
import com.example.daidaijie.syllabusapplication.model.OAModel;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.hoang8f.widget.FButton;
import io.realm.Realm;
import io.realm.RealmResults;

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

    int selectID;

    String inputKeyword;
    @BindView(R.id.showPageSelectImageView)
    ImageView mShowPageSelectImageView;
    @BindView(R.id.pageTitleLayout)
    LinearLayout mPageTitleLayout;
    @BindView(R.id.searchButton)
    FloatingActionButton mSearchButton;
    @BindView(R.id.titleLayout)
    LinearLayout mTitleLayout;
    @BindView(R.id.pageEditext)
    EditText mPageEditext;
    @BindView(R.id.gotoPageButton)
    FButton mGotoPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOAPagerAdapter = new OAPagerAdapter(getSupportFragmentManager());
        mContentViewPager.setAdapter(mOAPagerAdapter);
        mContentViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTitleTextView.setText("第 " + (position + 1) + " 页");
                animateIn(mSearchButton);
            }
        });
        mTitleTextView.setText("第 " + (mContentViewPager.getCurrentItem() + 1) + " 页");

        OAModel.getInstance().subID = 0;
        selectID = 0;
        OAModel.getInstance().keyword = "";
        inputKeyword = "";

        mSubDialog = new AlertDialog.Builder(this)
                .setTitle("选择部门")
                .setItems(OAModel.getInstance().getSubCompanysString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectSubButton.setText(
                                OAModel.getInstance().mSubCompanies.get(which).getSUBCOMPANYNAME());
                        selectID = OAModel.getInstance().mSubCompanies.get(which).getID();
                    }
                })
                .create();

        View view = getLayoutInflater().inflate(R.layout.dialog_search_oa, null, false);
        selectSubButton = (Button) view.findViewById(R.id.subButtonCard);
        selectSubButton.setText(OAModel.getInstance().mSubCompanies.get(0).getSUBCOMPANYNAME());
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
                        selectID = OAModel.getInstance().subID;
                        keywordEditText.setText(OAModel.getInstance().keyword);
                    }
                }).setPositiveButton("搜索", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OAModel.getInstance().subID = selectID;
                        OAModel.getInstance().keyword = keywordEditText.getText().toString().trim();
                        mOAPagerAdapter = new OAPagerAdapter(getSupportFragmentManager());
                        mContentViewPager.setAdapter(mOAPagerAdapter);
                        mContentViewPager.setCurrentItem(0);
                        mTitleTextView.setText("第 " + (mContentViewPager.getCurrentItem() + 1) + " 页");
                    }
                }).create();

        showPage(false);
        mTitleTextView.setOnClickListener(new View.OnClickListener() {
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
                if (mPageEditext.getText().toString().isEmpty()) {
                    mPageEditext.setError("输入不能为空");
                    return;
                }
                mContentViewPager.setCurrentItem(Integer.parseInt(mPageEditext.getText().toString()) - 1);
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
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmResults<OARead> results = realm.where(OARead.class).findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
            EventBus.getDefault().post(new OAClearEvent());
            realm.close();
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.searchButton)
    public void onClick() {
        mSearchDialog.show();
    }

    private void showPage(boolean isShow) {
        if (isShow) {
            mPageEditext.setText("");
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
}
