package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.BmobResult;
import com.example.daidaijie.syllabusapplication.model.TakeOutModel;
import com.example.daidaijie.syllabusapplication.service.TakeOutInfoService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TakeOutActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getTakeOutInfo();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_take_out;
    }

    private void getTakeOutInfo() {
        TakeOutInfoService service = TakeOutModel.getInstance().mRetrofit.create(TakeOutInfoService.class);
        service.getTokenResult("name,long_number,short_number,condition")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BmobResult>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(TakeOutActivity.this, "OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Logger.t("takeout").e(e.getMessage());
                        Toast.makeText(TakeOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BmobResult bmobResult) {
                        Logger.t("takeout").json(GsonUtil.getDefault().toJson(bmobResult));
                    }
                });
    }
}
