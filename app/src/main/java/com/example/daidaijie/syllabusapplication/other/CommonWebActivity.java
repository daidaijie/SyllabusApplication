package com.example.daidaijie.syllabusapplication.other;

import android.content.Context;
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

public class CommonWebActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.loadingBar)
    ProgressBar mProgressBar;

    private static final String EXTRA_URL = CommonWebActivity.class.getCanonicalName() + ".url";

    private static final String EXTRA_TITLE = CommonWebActivity.class.getCanonicalName() + ".title";

    private boolean hasTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        WebSettings setting = mWebView.getSettings();
        setting.setAllowFileAccess(true);
        setting.setAppCacheEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        String title = getIntent().getStringExtra(EXTRA_TITLE);
        if (title.isEmpty()) {
            hasTitle = false;
        } else {
            hasTitle = true.;
            mTitleTextView.setText(title);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (!hasTitle) {
                    mTitleTextView.setText(title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress >= 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }

                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.loadUrl(getIntent().getStringExtra(EXTRA_URL));

        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Toast.makeText(CommonWebActivity.this, "url: " + url, Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
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

    public static Intent getIntent(Context context, String url) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra(EXTRA_URL, url);

        return intent;
    }

    public static Intent getIntent(Context context, String url, String title) {
        Intent intent = new Intent(context, CommonWebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);

        return intent;
    }
}
