package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.OABean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;

public class OADetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.oAWebView)
    WebView mOAWebView;

    OABean mOABean;

    String oaContent = "";

    String label = "!@#$%^&*";

    public static final String EXTRA_OABEAN = "com.example.daidaijie.syllabusapplication.activity" +
            ".OADetailActivity.OABean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOAWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        mOABean = (OABean) getIntent().getSerializableExtra(EXTRA_OABEAN);
        oaContent = mOABean.getDOCCONTENT();
        int index = oaContent.indexOf(label) + label.length();
        oaContent = oaContent.substring(index);

        Document doc = Jsoup.parse(getHtml());
        Element div = doc.select("div#div_doc").first();
        div.append(oaContent);
        div = doc.select("div#div_accessory").first();

        if (mOABean.getACCESSORYCOUNT() == 0) {
            div.remove();
        } else {

        }

        mOAWebView.loadData(doc.toString(), "text/html; charset=UTF-8", null);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_oadetail;
    }

    public static Intent getIntent(Context context, OABean oaBean) {
        Intent intent = new Intent(context, OADetailActivity.class);
        intent.putExtra(EXTRA_OABEAN, oaBean);
        return intent;
    }

    private String getHtml() {
        StringBuffer htmlData = new StringBuffer();
        try {
            InputStreamReader isr = new InputStreamReader(
                    getResources().getAssets().open("index.html"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            char[] tempChar = new char[1024];
            int charRead = 0;

            while ((charRead = br.read(tempChar)) != -1) {
                htmlData.append(tempChar);
            }
            return htmlData.toString();
        } catch (IOException e) {
        }
        return "";
    }
}
