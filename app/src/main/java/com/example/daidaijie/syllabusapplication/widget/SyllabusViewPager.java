package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以禁止滑动的ViewPager
 * Created by daidaijie on 2016/7/26.
 */
public class SyllabusViewPager extends ViewPager {

    private boolean scrollable = true;

    public SyllabusViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollable) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void scrollTo(int x, int y){
        if (scrollable){
            super.scrollTo(x, y);
        }
    }

    public boolean isScrollable() {
        return scrollable;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }


}
