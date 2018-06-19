package com.example.daidaijie.syllabusapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;

import butterknife.BindView;

public class EmailWebActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webView)
    WebView mEmailWebView;
    @BindView(R.id.loadingBar)
    ProgressBar mEmailLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);
        mTitleTextView.setText("校内邮箱");

        WebSettings setting = mEmailWebView.getSettings();
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setJavaScriptEnabled(true);
        mEmailWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        mEmailWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mEmailLoadingBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    mEmailLoadingBar.setVisibility(View.GONE);
                } else {

                    mEmailLoadingBar.setVisibility(View.VISIBLE);
                }

                super.onProgressChanged(view, newProgress);
            }
        });
        mEmailWebView.loadUrl("https://partner.outlook.cn/owa/?realm=stu.edu.cn&exsvurl=1&ll-cc=2052&modurl=0");

        mEmailWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Toast.makeText(EmailWebActivity.this, "url: " + url, Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mEmailWebView.canGoBack()) {
            mEmailWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_email_web;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
