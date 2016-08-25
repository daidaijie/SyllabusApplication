package com.example.daidaijie.syllabusapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.User;
import com.example.daidaijie.syllabusapplication.presenter.LoginPresenter;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.view.ILoginView;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements ILoginView, View.OnClickListener {

    @BindView(R.id.titleLayout)
    LinearLayout mTitleLayout;
    @BindView(R.id.inputLayout)
    LinearLayout mInputLayout;
    @BindView(R.id.loginButton)
    Button mLoginButton;
    @BindView(R.id.tipTextView)
    TextView mTipTextView;
    @BindView(R.id.usernameEditText)
    EditText mUsernameEditText;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;

    AlertDialog mAlertDialog;

    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attach(this);

        mTipTextView.setText(Html.fromHtml("<u>注意事项</u>"));

        mUsernameEditText.setText(User.getInstance().getAccount());
        mPasswordEditText.setText(User.getInstance().getPassword());

        mAlertDialog = LoadingDialogBuiler.getLoadingDialog(this,
                getResources().getColor(R.color.colorPrimary));

        mLoginButton.setOnClickListener(this);


    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detach();
    }

    @Override
    protected boolean isCanBack() {
        return false;
    }

    @Override
    public void onClick(View v) {

        if (mUsernameEditText.getText().toString().trim().isEmpty()) {
            mUsernameEditText.setError("账号名不能为空");
            return;
        }
        if (mPasswordEditText.getText().toString().trim().isEmpty()) {
            mPasswordEditText.setError("密码不能为空");
            return;
        }

        mLoginPresenter.login(
                mUsernameEditText.getText().toString(),
                mPasswordEditText.getText().toString()
        );
    }

    @Override
    public void showLoginSuccess() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void showLoginFail() {
        SnackbarUtil.ShortSnackbar(mPasswordEditText, "登录失败", SnackbarUtil.Alert).show();
    }

    @Override
    public void showLoadingDialog() {
        mAlertDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mAlertDialog.dismiss();
    }
}
