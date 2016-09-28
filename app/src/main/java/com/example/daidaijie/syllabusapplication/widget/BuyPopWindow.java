package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.FitWindowsFrameLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.liaoinstan.springview.utils.DensityUtil;

import java.util.List;

/**
 * Created by daidaijie on 2016/9/23.
 */

public class BuyPopWindow extends PopupWindow {


    private View mView;

    private Context mContext;

    private View mEmptyView;

    public BuyPopWindow(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.pop_buy_takeout, null);


        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);

        mEmptyView = mView.findViewById(R.id.emptyView);
        mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyPopWindow.this.dismiss();
            }
        });

    }


}
