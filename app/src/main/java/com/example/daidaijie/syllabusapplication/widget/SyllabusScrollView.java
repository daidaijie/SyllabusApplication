package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

/**
 * Created by daidaijie on 2016/7/17.
 * 避免ScrollView和SwipeRefreshLayout滑动冲突
 */
public class SyllabusScrollView extends ScrollView {

    private SwipeRefreshLayout swipeRefreshLayout;

    public SyllabusScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t != 0) {
            swipeRefreshLayout.setEnabled(false);
        } else {
            swipeRefreshLayout.setEnabled(true);
        }
    }

}
