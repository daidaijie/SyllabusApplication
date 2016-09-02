package com.example.daidaijie.syllabusapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.LoginInfo;
import com.example.daidaijie.syllabusapplication.bean.StreamInfo;
import com.example.daidaijie.syllabusapplication.model.InternetModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.service.InterenetService;
import com.example.daidaijie.syllabusapplication.service.LoginInternetService;
import com.example.daidaijie.syllabusapplication.services.StreamService;
import com.example.daidaijie.syllabusapplication.util.GsonUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import butterknife.BindView;
import info.hoang8f.widget.FButton;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class LoginInternetActivity extends BaseActivity {

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.usernameEditText)
    EditText mUsernameEditText;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;
    @BindView(R.id.loginButton)
    FButton mLoginButton;

    AlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mLoadingDialog = LoadingDialogBuiler.getLoadingDialog(
                this, ThemeModel.getInstance().colorPrimary
        );

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login_internet;
    }


    private void login() {
        mLoadingDialog.show();

        InterenetService interenetService = InternetModel.getInstance().mRetrofit.create(InterenetService.class);


        final LoginInternetService loginInternetService = InternetModel.getInstance().mRetrofit.create(
                LoginInternetService.class
        );

        interenetService.getInternetInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        Document doc = Jsoup.parse(s);
                        Element tables = doc.getElementsByTag("table").first();
                        Elements trs = tables.select("tr");

                        //少于2证明没登陆
                        if (trs.size() < 2) {
                            return true;
                        }
                        SnackbarUtil.ShortSnackbar(mLoginButton, "用户已在线", SnackbarUtil.Info).show();
                        return false;
                    }
                }).observeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        return loginInternetService.loginInternet(
                                "pwdLogin",
                                mUsernameEditText.getText().toString().trim(),
                                mPasswordEditText.getText().toString().trim(),
                                1
                        );
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<String, LoginInfo>() {
                    @Override
                    public LoginInfo call(String s) {
                        return GsonUtil.getDefault().fromJson(
                                s.replaceAll("'", "\""), LoginInfo.class
                        );
                    }
                })
                .subscribe(new Subscriber<LoginInfo>() {
                    @Override
                    public void onCompleted() {
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackbarUtil.ShortSnackbar(mLoginButton, "登录失败", SnackbarUtil.Alert).show();
                        Toast.makeText(LoginInternetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        mLoadingDialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        SnackbarUtil.ShortSnackbar(mLoginButton, loginInfo.getMsg(), SnackbarUtil.Info).show();
                    }
                });
    }
}
