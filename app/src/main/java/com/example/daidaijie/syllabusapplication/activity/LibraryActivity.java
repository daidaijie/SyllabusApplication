package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.adapter.LibraryPagerAdapter;
import com.example.daidaijie.syllabusapplication.adapter.OAPagerAdapter;
import com.example.daidaijie.syllabusapplication.model.LibraryModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

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

    LibraryPagerAdapter mLibraryPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeModel.getInstance().colorPrimary);


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLibraryPagerAdapter = new LibraryPagerAdapter(getSupportFragmentManager(),
                        LibraryModel.getInstance().searchWords.get(0), mSearchEditText.getText().toString().trim());
                mLibraryViewPager.setAdapter(mLibraryPagerAdapter);
                mLibraryViewPager.setCurrentItem(0);
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_library;
    }
}
