package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.BookDetailBean;
import com.example.daidaijie.syllabusapplication.bean.LibraryBean;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class BookDetailActivity extends BaseActivity implements BookDetailContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bookDetailWebView)
    WebView mBookDetailWebView;
    @BindView(R.id.bookNameTextView)
    TextView mBookNameTextView;
    @BindView(R.id.authorTextView)
    TextView mAuthorTextView;
    @BindView(R.id.pressTextView)
    TextView mPressTextView;
    @BindView(R.id.pressDateTextView)
    TextView mPressDateTextView;
    @BindView(R.id.resultLinearLayout)
    LinearLayout mResultLinearLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static final String EXTRA_LIBRARY_BEAN_POS = BookDetailActivity.class.getCanonicalName()
            + ".position";

    private static final String EXTRA_LIBRARY_BEAN_SUB_POS = BookDetailActivity.class.getCanonicalName()
            + ".subPosition";

    @Inject
    BookDetailPresenter mBookDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        WebSettings settings = mBookDetailWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mBookDetailWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        mBookDetailWebView.setWebViewClient(new MyWebViewClient());

        setupSwipeRefreshLayout(mSwipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        DaggerBookDetailComponent.builder()
                .bookDetailModule(new BookDetailModule(this,
                        getIntent().getIntExtra(EXTRA_LIBRARY_BEAN_POS, 0),
                        getIntent().getIntExtra(EXTRA_LIBRARY_BEAN_SUB_POS, 0)))
                .libModelComponent(LibModelComponent.getInstance())
                .build().inject(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mBookDetailPresenter.start();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_book_detail;
    }

    public static Intent getIntent(Context context, int position, int subPosition) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_LIBRARY_BEAN_POS, position);
        intent.putExtra(EXTRA_LIBRARY_BEAN_SUB_POS, subPosition);

        return intent;
    }

    @Override
    public void showLoading(boolean isShow) {
        mSwipeRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void loadUrl(String url) {
        mBookDetailWebView.loadUrl(url);
    }

    @Override
    public void showData(LibraryBean libraryBean) {
        mBookNameTextView.setText(libraryBean.getName());
        mAuthorTextView.setText(libraryBean.getAuthor());
        mPressTextView.setText(libraryBean.getPress());
        mPressDateTextView.setText(libraryBean.getPressDate());
    }

    @Override
    public void showResult(List<BookDetailBean> mBookDetailBeen) {
        mResultLinearLayout.removeAllViews();
        for (int i = 0; i <= mBookDetailBeen.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.item_book_locate, null, false);
            TextView mBookLocateTextView = (TextView) view.findViewById(R.id.bookLocateTextView);
            TextView mBookStateTextView = (TextView) view.findViewById(R.id.bookStateTextView);
            TextView mBookTypeTextView = (TextView) view.findViewById(R.id.bookTypeTextView);
            View mDivLine = view.findViewById(R.id.div_line);
            mDivLine.setVisibility(View.VISIBLE);

            if (i == 0) {
                mBookLocateTextView.setText(Html.fromHtml("<b>馆藏地</b>"));
                mBookStateTextView.setText(Html.fromHtml("<b>状态</b>"));
                mBookTypeTextView.setText(Html.fromHtml("<b>借阅类型</b>"));
            } else {
                BookDetailBean bookDetailBean = mBookDetailBeen.get(i - 1);
                mBookLocateTextView.setText(bookDetailBean.getBook());
                mBookStateTextView.setText(bookDetailBean.getState());
                mBookTypeTextView.setText(bookDetailBean.getType());

                LoggerUtil.e("html", bookDetailBean.getBook() + "," + bookDetailBean.getState() + "," + bookDetailBean.getType());
            }

            mResultLinearLayout.addView(view);
        }
        mResultLinearLayout.requestLayout();
    }

    @Override
    public void showInfoMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mResultLinearLayout, msg, SnackbarUtil.Info).show();
    }

    @Override
    public void onRefresh() {
        mBookDetailWebView.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
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
            BookDetailActivity.this.mBookDetailPresenter.handlerHtml(html);
        }
    }

}
