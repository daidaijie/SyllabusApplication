package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.LibraryModel;
import com.example.daidaijie.syllabusapplication.service.LibraryService;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LibraryActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.searchEditText)
    EditText mSearchEditText;
    @BindView(R.id.searchButton)
    Button mSearchButton;
    @BindView(R.id.ansTextView)
    TextView mAnsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final LibraryService libraryService = LibraryModel.getInstance().mRetrofit.create(LibraryService.class);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    libraryService.getLibrary(
                            URLEncoder.encode(mSearchEditText.getText().toString().trim(), "gb2312"),
                            "ALL",
                            "ALL",
                            "ALL",
                            "M_PUB_YEAR",
                            "DESC",
                            1,
                            20,
                            "table")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<String>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    mAnsTextView.setText(e.getMessage());
                                }

                                @Override
                                public void onNext(String s) {
                                    Element body = Jsoup.parseBodyFragment(s).body();
                                    Element table = body.select("table.tb").first();
                                    Elements items = table.getElementsByTag("tr");
                                    items.remove(0);
                                    StringBuilder sb = new StringBuilder();
                                    for (Element item : items) {
                                        Element name = item.getElementsByTag("td").get(1);
                                        sb.append(name.text()+"\n");
                                    }
                                    mAnsTextView.setText(sb.toString());
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_library;
    }
}
