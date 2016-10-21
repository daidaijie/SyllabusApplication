package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;

public class BookDetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bookDetailWebView)
    WebView mBookDetailWebView;
    @BindView(R.id.webInfoTextView)
    TextView mWebInfoTextView;

    private AlertDialog mLoadingDialog;

    private LibraryBean mLibraryBean;

    private String url;

    public static final String EXTRA_LIBRARY_BEAN = "com.example.daidaijie.syllabusapplication.activity" +
            ".BookDetailActivity.mLibraryBean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(this, ThemeModel.getInstance().colorPrimary);
        mLoadingDialog.show();

        mLibraryBean = (LibraryBean) getIntent().getSerializableExtra(EXTRA_LIBRARY_BEAN);

        url = "http://opac.lib.stu.edu.cn:83/opac/" + mLibraryBean.getUrl();

        WebSettings settings = mBookDetailWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mBookDetailWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mBookDetailWebView.setWebViewClient(new MyWebViewClient());
        mBookDetailWebView.loadUrl(url);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_book_detail;
    }

    public static Intent getIntent(Context context, LibraryBean libraryBean) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_LIBRARY_BEAN, libraryBean);
        return intent;
    }

    final class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d("HTML", "onPageStarted: ");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            Log.d("HTML", "onPageFinished: ");
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                }
            }, 2000);
            super.onPageFinished(view, url);
        }
    }

    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            Element body = Jsoup.parseBodyFragment(html).body();
            Element table = body.select("table.tb").first();
            Elements items = table.getElementsByTag("tr");
            final Element span = items.get(1).getElementsByTag("td").first().getElementsByTag("span").first();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebInfoTextView.setText(span.text());
                    mLoadingDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        mLoadingDialog.dismiss();
        super.onBackPressed();
    }
}
