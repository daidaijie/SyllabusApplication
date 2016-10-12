package com.example.daidaijie.syllabusapplication.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.daidaijie.syllabusapplication.App;
import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu.LibraryFragment;

import butterknife.ButterKnife;
import cn.nekocode.emojix.Emojix;

/**
 * Created by daidaijie on 2016/7/25.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected int deviceWidth;
    protected int deviceHeight;

    protected AppComponent mAppComponent;

    protected static final String CLASS_NAME = LibraryFragment.class.getCanonicalName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAppComponent = ((App) getApplication()).getAppComponent();

        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);

        setTheme(ThemeModel.getInstance().getStyle());

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        ThemeModel.getInstance().colorPrimary = typedValue.data;

        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        ThemeModel.getInstance().colorPrimaryDark = typedValue.data;
        setContentView(getContentView());


        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        deviceWidth = getWindowManager().getDefaultDisplay().getWidth();
        deviceHeight = getWindowManager().getDefaultDisplay().getHeight();


    }

    protected abstract int getContentView();

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void setupToolbar(Toolbar toolbar) {
        //透明状态栏并且适应Toolbar的高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams layoutParams = toolbar.getLayoutParams();
            layoutParams.height = layoutParams.height + getStatusBarHeight();
        } else {
            deviceHeight -= getStatusBarHeight();
        }
    }

    protected void setupSwipeRefreshLayout(SwipeRefreshLayout mRefreshLayout) {
        mRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Emojix.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //判断是返回键然后退出当前Activity
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!isCanBack()) return;
        super.onBackPressed();
    }

    protected boolean isCanBack() {
        return true;
    }

    protected void showInput(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    protected void hideInput(EditText editText) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}