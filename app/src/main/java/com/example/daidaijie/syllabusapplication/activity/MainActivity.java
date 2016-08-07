package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.example.daidaijie.syllabusapplication.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.convenientBanner)
    ConvenientBanner mConvenientBanner;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.query_syllabus_button)
    LinearLayout mQuerySyllabusButton;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbarLayout.setTitle("");
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
//添加toolbar drawer的开关
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        List<Uri> uris = new ArrayList<>();

        uris.add(Uri.parse("http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg"));
        uris.add(Uri.parse("http://img2.3lian.com/2014/f2/37/d/39.jpg"));
        uris.add(Uri.parse("http://img2.3lian.com/2014/f2/37/d/39.jpg"));

        mConvenientBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerImageHolderView();
            }
        }, uris).setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    private class BannerImageHolderView implements Holder<Uri> {
        SimpleDraweeView draweeView;

        @Override
        public View createView(Context context) {
            draweeView = new SimpleDraweeView(context);
            return draweeView;
        }

        @Override
        public void UpdateUI(Context context, int position, Uri data) {
            draweeView.setImageURI(data);
        }

    }

    // 开始自动翻页
    @Override
    protected void onResume() {
        super.onResume();
        //开始自动翻页
        mConvenientBanner.startTurning(3000);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
