package com.example.daidaijie.syllabusapplication.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.titleLayout)
    LinearLayout mTitleLayout;
    @BindView(R.id.inputLayout)
    LinearLayout mInputLayout;
    @BindView(R.id.loginButton)
    Button mLoginButton;
    @BindView(R.id.tipTextView)
    TextView mTipTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTipTextView.setText(Html.fromHtml("<u>注意事项</u>"));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }
}
