package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.OABean;
import com.example.daidaijie.syllabusapplication.bean.OAFileBean;
import com.example.daidaijie.syllabusapplication.model.OAModel;
import com.example.daidaijie.syllabusapplication.service.OAFileService;
import com.example.daidaijie.syllabusapplication.util.AssetUtil;
import com.example.daidaijie.syllabusapplication.util.FileUtil;
import com.example.daidaijie.syllabusapplication.util.RetrofitUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OADetailActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.oAWebView)
    WebView mOAWebView;
    @BindView(R.id.oaFileLinearLayout)
    LinearLayout mOaFileLinearLayout;

    OABean mOABean;

    List<OAFileBean> mOAFileBeen;

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
        mOAWebView.getSettings().setLoadWithOverviewMode(true);
        mOAWebView.getSettings().setUseWideViewPort(true);
        mOAWebView.getSettings().setJavaScriptEnabled(true);

        mOABean = (OABean) getIntent().getSerializableExtra(EXTRA_OABEAN);
        oaContent = mOABean.getDOCCONTENT();
        int index = oaContent.indexOf(label) + label.length();
        oaContent = oaContent.substring(index);

        Document contentDoc = Jsoup.parse(oaContent);

        Elements imgs = contentDoc.select("img");
        for (Element img : imgs) {
            imgs.attr("style", "width: 100%;");
        }

        Elements tables = contentDoc.getElementsByTag("table");
        for (Element table : tables) {
            table.attr("width", "100%");
            table.attr("style", "width: 100%;");
            Elements trs = table.select("tr");
            for (Element tr : trs) {
                Elements tds = tr.select("td");
                double witdh = 100.0 / tds.size();
                for (Element td : tds) {
                    String colspan = td.attr("colspan");
                    if (colspan.trim().isEmpty()) {
                        td.attr("style", "width:" + witdh + "%");
                    } else {
                        td.attr("style", "width:" + witdh * Integer.parseInt(colspan) + "%");
                    }
                    td.removeAttr("nowrap");
//                    Toast.makeText(OADetailActivity.this, td.attr("style"), Toast.LENGTH_SHORT).show();
                }
            }
        }


        Document doc = Jsoup.parse(AssetUtil.getStringFromPath("index.html"));
        Element div = doc.select("div#div_doc").first();
        div.append(contentDoc.toString());
        div = doc.select("div#div_accessory").first();

        if (mOABean.getACCESSORYCOUNT() == 0) {
            div.remove();
        } else {
        }

        mOAWebView.setDrawingCacheEnabled(true);
        mOAWebView.loadData(doc.toString(), "text/html; charset=UTF-8", null);

        if (mOABean.getACCESSORYCOUNT() != 0) {
            getOAFile();
        }
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
            screenShot();
        }
        return super.onOptionsItemSelected(item);
    }

    private void screenShot() {
        Bitmap webViewScreen = captureScreen();
        if (webViewScreen != null) {
            try {
                saveFile(webViewScreen, "STUOA" + SystemClock.currentThreadTimeMillis() + ".jpg", "STUOA");
                Toast.makeText(this, "已保存到图库", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OADetailActivity.this, "图片太大，无法截取", Toast.LENGTH_SHORT).show();
        }
    }


    private Bitmap captureScreen() {
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

    public static void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String subForder = FileUtil.get_app_folder(true) + path;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(myCaptureFile);
        intent.setData(uri);
        App.getContext().sendBroadcast(intent);
    }

    private void getOAFile() {
        OAFileService oaFileService = OAModel.getInstance().mRetrofit.create(OAFileService.class);
        oaFileService.getOAFileList("undefined", mOABean.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<OAFileBean>>() {
                    @Override
                    public void onCompleted() {
                        showOAFile();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<OAFileBean> oaFileBeen) {
                        mOAFileBeen = oaFileBeen;
                    }
                });
    }

    private void showOAFile() {
        mOaFileLinearLayout.removeAllViews();

        View tipView = getLayoutInflater().inflate(R.layout.item_oa_file, null, false);
        TextView mTipTextView = (TextView) tipView.findViewById(R.id.fileNameTextView);
        Button mTipButton = (Button) tipView.findViewById(R.id.downFileButton);
        mTipButton.setVisibility(View.GONE);
        mTipTextView.setText(Html.fromHtml("<b>附件</b> (PS: 附件只能在内网上下载)"));
        mTipTextView.setTextSize(16);
        mTipTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        mOaFileLinearLayout.addView(tipView);

        for (int i = 0; i < mOAFileBeen.size(); i++) {
            final OAFileBean oaFileBean = mOAFileBeen.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_oa_file, null, false);
            TextView mFileNameTextView = (TextView) view.findViewById(R.id.fileNameTextView);
            Button mDownFileButton = (Button) view.findViewById(R.id.downFileButton);

            mFileNameTextView.setText(oaFileBean.getIMAGEFILENAME());

            mDownFileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "http://notes.stu.edu.cn/weaver/weaver.file.FileDownload?fileid="
                            + oaFileBean.getIMAGEFILENAME() + "&download=1&requestid=0";
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    intent.setData(Uri.parse(url));

                    startActivity(intent);
                }
            });
            mOaFileLinearLayout.addView(view);
        }
    }
}
