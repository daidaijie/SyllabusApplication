package com.example.daidaijie.syllabusapplication.other;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import com.example.daidaijie.syllabusapplication.util.ClipboardUtil;
import com.example.daidaijie.syllabusapplication.util.ShareWXUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.ShareWXDialog;

import butterknife.BindView;

public class CommonWebActivity extends BaseActivity implements ShareWXDialog.OnShareSelectCallBack {

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
            hasTitle = true;
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
        int id = item.getItemId();
        if (id == R.id.action_share_url) {
            showShareDialog();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_common_web, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void showShareDialog() {
        ShareWXDialog dialog = new ShareWXDialog();
        dialog.setOnShareSelectCallBack(this);
        dialog.show(getSupportFragmentManager());
    }

    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mWebView, msg, SnackbarUtil.Info).show();
    }

    @Override
    public void onShareSelect(int position) {
        if (position == 0) {
            share(0);
        } else if (position == 1) {
            share(1);
        } else {
            ClipboardUtil.copyToClipboard(mWebView.getUrl());
            showInfoMessage("已复制链接到剪贴板");
        }
    }

    private void share(int scene) {
        ShareWXUtil.shareUrl(mWebView.getUrl(), mWebView.getTitle(), "汕大课程表分享",
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_syllabus_icon), scene
        );
    }
}
