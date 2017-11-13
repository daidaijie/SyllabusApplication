package com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.OAFileBean;
import com.example.daidaijie.syllabusapplication.util.ThemeUtil;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAModelComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OADetailActivity extends BaseActivity implements OADetailContract.view {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.oAWebView)
    WebView mOAWebView;
    @BindView(R.id.oaFileLinearLayout)
    LinearLayout mOaFileLinearLayout;

    public static final String EXTRA_OA_POSITION = OADetailActivity.class.getCanonicalName() + ".position";
    public static final String EXTRA_OA_SUB_POSITION = OADetailActivity.class.getCanonicalName() + ".subPosition";

    @Inject
    OADetailPresenter mOADetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTitleBar(mToolbar);

        mOAWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mOAWebView.getSettings().setLoadWithOverviewMode(true);
        mOAWebView.getSettings().setUseWideViewPort(true);
        mOAWebView.getSettings().setJavaScriptEnabled(true);
        mOAWebView.setDrawingCacheEnabled(true);

        DaggerOADetailComponent.builder()
                .oAModelComponent(OAModelComponent.getINSTANCE())
                .oADetailModule(new OADetailModule(this,
                        getIntent().getIntExtra(EXTRA_OA_POSITION, 0),
                        getIntent().getIntExtra(EXTRA_OA_SUB_POSITION, 0)))
                .build().inject(this);

        mOADetailPresenter.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_oadetail;
    }

    public static Intent getIntent(Context context, int position, int subPosition) {
        Intent intent = new Intent(context, OADetailActivity.class);
        intent.putExtra(EXTRA_OA_POSITION, position);
        intent.putExtra(EXTRA_OA_SUB_POSITION, subPosition);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_oa_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_screenshot) {
            mOADetailPresenter.screenShot();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Bitmap captureScreen() {
        Picture picture = mOAWebView.capturePicture();
        try {
            Bitmap b = Bitmap.createBitmap(picture.getWidth(),
                    picture.getHeight(), Bitmap.Config.ARGB_4444);
            Canvas c = new Canvas(b);
            picture.draw(c);
            return b;
        } catch (OutOfMemoryError error) {
            return null;
        }
    }

    @Override
    public void showView(String webViewString) {
        mOAWebView.loadData(webViewString, "text/html; charset=UTF-8", null);
    }

    @Override
    public void showOAFile(List<OAFileBean> oaFileBeen) {
        mOaFileLinearLayout.removeAllViews();

        View tipView = getLayoutInflater().inflate(R.layout.item_oa_file, null, false);
        TextView mTipTextView = (TextView) tipView.findViewById(R.id.fileNameTextView);
        Button mTipButton = (Button) tipView.findViewById(R.id.downFileButton);
        mTipButton.setVisibility(View.GONE);
        mTipTextView.setText(Html.fromHtml("<b>附件</b> (PS: 附件只能在内网上下载)"));
        mTipTextView.setTextSize(16);
        mTipTextView.setTextColor(ThemeUtil.getInstance().colorPrimary);
        mOaFileLinearLayout.addView(tipView);

        for (int i = 0; i < oaFileBeen.size(); i++) {
            final OAFileBean oaFileBean = oaFileBeen.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_oa_file, null, false);
            TextView mFileNameTextView = (TextView) view.findViewById(R.id.fileNameTextView);
            Button mDownFileButton = (Button) view.findViewById(R.id.downFileButton);

            mFileNameTextView.setText(oaFileBean.getIMAGEFILENAME());

            mDownFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://oa.stu.edu.cn/weaver/weaver.file.FileDownload?fileid="
                            + oaFileBean.getIMAGEFILEID() + "&download=1&requestid=0";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
            mOaFileLinearLayout.addView(view);
        }
    }

    @Override
    public void showFileLoadFailMessage(String msg) {
        SnackbarUtil.LongSnackbar(
                mOAWebView, msg, SnackbarUtil.Alert
        ).setAction("再次获取", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOADetailPresenter.loadData();
            }
        }).show();
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mOAWebView, msg, SnackbarUtil.Alert
        ).show();
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mOAWebView, msg, SnackbarUtil.Confirm
        ).show();

    }
}
