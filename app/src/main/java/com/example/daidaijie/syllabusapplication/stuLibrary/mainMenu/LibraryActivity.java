package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibraryPagerAdapter;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.LibSearchBean;
import com.example.daidaijie.syllabusapplication.event.LibPageCountEvent;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibraryUtil;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class LibraryActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchEditText)
    EditText mSearchEditText;
    @BindView(R.id.searchButton)
    Button mSearchButton;
    AlertDialog mLoadingDialog;
    @BindView(R.id.libraryViewPager)
    ViewPager mLibraryViewPager;
    @BindView(R.id.searchRangeButton)
    Button mSearchRangeButton;
    @BindView(R.id.searchSFButton)
    Button mSearchSFButton;
    @BindView(R.id.searchOBButton)
    Button mSearchOBButton;
    @BindView(R.id.queryLayout)
    LinearLayout mQueryLayout;
    @BindView(R.id.showQuerySelectImageView)
    ImageView mShowQuerySelectImageView;
    @BindView(R.id.showQueryLayout)
    LinearLayout mShowQueryLayout;
    @BindView(R.id.libCountTextView)
    TextView mLibCountTextView;

    AlertDialog mSearchRangeDialog;
    AlertDialog mSearchOBDialog;
    AlertDialog mSearchSFDialog;
    int searchWordWhich;
    int searchSFWhich;
    int searchOBWhich;

    LibraryPagerAdapter mLibraryPagerAdapter;

    public LibModelComponent mLibModelComponent;

    LibSearchBean mLibSearchBean;

    private LibraryUtil mLibraryUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setupTitleBar(mToolbar);

        mLibraryUtil = new LibraryUtil();

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeUtil.getInstance().colorPrimary);

        final String[] rangeItems = getResources().getStringArray(R.array.query_lib_string);
        searchWordWhich = 0;
        mSearchRangeButton.setText(rangeItems[searchWordWhich]);
        mSearchRangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchRangeDialog.show();
            }
        });
        mSearchRangeDialog = new AlertDialog.Builder(this)
                .setTitle("检索范围")
                .setItems(R.array.query_lib_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchWordWhich = which;
                        mSearchRangeButton.setText(rangeItems[searchWordWhich]);
                    }
                }).create();

        final String[] sfItems = getResources().getStringArray(R.array.query_lib_sf_string);
        searchSFWhich = 0;
        mSearchSFButton.setText(sfItems[searchSFWhich]);
        mSearchSFButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchSFDialog.show();
            }
        });
        mSearchSFDialog = new AlertDialog.Builder(this)
                .setTitle("排序方式")
                .setItems(R.array.query_lib_sf_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchSFWhich = which;
                        mSearchSFButton.setText(sfItems[searchSFWhich]);
                    }
                }).create();

        final String[] obItems = getResources().getStringArray(R.array.query_lib_ob_string);
        searchOBWhich = 0;
        mSearchOBButton.setText(obItems[searchOBWhich]);
        mSearchOBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchOBDialog.show();
            }
        });
        mSearchOBDialog = new AlertDialog.Builder(this)
                .setTitle("排序方式")
                .setItems(R.array.query_lib_ob_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        searchOBWhich = which;
                        mSearchOBButton.setText(obItems[searchOBWhich]);
                    }
                }).create();

        mLibCountTextView.setVisibility(View.GONE);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLibCountTextView.setVisibility(View.GONE);
                mLibSearchBean = new LibSearchBean();
                mLibSearchBean.setTag(mLibraryUtil.searchWords.get(searchWordWhich));
                mLibSearchBean.setWord(mSearchEditText.getText().toString().trim());
                mLibSearchBean.setOb(mLibraryUtil.libOBs.get(searchOBWhich));
                mLibSearchBean.setSf(mLibraryUtil.libSFs.get(searchSFWhich));

                LibModelComponent.destroy();
                mLibModelComponent = LibModelComponent.getInstance(mAppComponent, mLibSearchBean);

                mLibraryPagerAdapter = new LibraryPagerAdapter(getSupportFragmentManager());
                mLibraryViewPager.setAdapter(mLibraryPagerAdapter);
                mLibraryViewPager.setCurrentItem(0);
            }
        });

        showQuery(true);
        mShowQueryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mQueryLayout.getVisibility() == View.GONE) {
                    showQuery(true);
                } else {
                    showQuery(false);
                }
            }
        });

        mLibraryViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (mQueryLayout.getVisibility() == View.GONE) {
                    showPageNum(position);
                }
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_library;
    }

    private void showQuery(boolean isShow) {
        if (isShow) {
            mShowQuerySelectImageView.setRotation(180.0f);
            mQueryLayout.setVisibility(View.VISIBLE);
            mTitleTextView.setText("图书检索");
        } else {
            mShowQuerySelectImageView.setRotation(0.0f);
            mQueryLayout.setVisibility(View.GONE);
            showPageNum(mLibraryViewPager.getCurrentItem());
        }
    }

    private void showPageNum(int position) {
        mTitleTextView.setText("第 " + (position + 1) + " 页");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPageCount(LibPageCountEvent event) {
        if (event.count == 0) {
            mLibraryPagerAdapter.setPageCount(1);
        } else {
            mLibraryPagerAdapter.setPageCount((event.count - 1) / 10 + 1);
        }
        mLibCountTextView.setVisibility(View.VISIBLE);
        mLibCountTextView.setText("共检索到" + event.count + "本图书");
        mLibraryPagerAdapter.notifyDataSetChanged();
        showQuery(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LibModelComponent.destroy();
        EventBus.getDefault().unregister(this);
    }
}
