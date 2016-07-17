package com.example.daidaijie.syllabusapplication;


import android.animation.Animator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SyllabusFragment extends Fragment {

    /**
     * 布局
     */
    private GridLayout syllabusGridLayout;
    private LinearLayout dateLinearLayout;
    private LinearLayout timeLinearLayout;
    private SyllabusScrollView syllabusScrollView;
    private SwipeRefreshLayout syllabusRefreshLayout;

    /**
     * 长宽,像素为单位
     */
    private int deviceWidth;
    private int devideHeight;
    private int timeWidth;
    private int gridWidth;
    private int gridHeight;


    public static SyllabusFragment newInstance() {
        SyllabusFragment fragment = new SyllabusFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        syllabusGridLayout = (GridLayout) view.findViewById(R.id.syllabusGridLayout);
        dateLinearLayout = (LinearLayout) view.findViewById(R.id.dateLinearLayout);
        timeLinearLayout = (LinearLayout) view.findViewById(R.id.timeLinearLayout);
        syllabusRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.syllabusRefreshLayout);
        syllabusScrollView = (SyllabusScrollView) view.findViewById(R.id.syllabusScrollView);

        syllabusRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorAccent,
                android.R.color.holo_green_light,
                android.R.color.holo_purple
        );

        syllabusScrollView.setSwipeRefreshLayout(syllabusRefreshLayout);
        syllabusRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(
                                syllabusGridLayout,
                                "课表同步成功",
                                Snackbar.LENGTH_SHORT
                        ).show();
                        syllabusRefreshLayout.setRefreshing(false);
                    }
                },2000);
            }
        });

        deviceWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        devideHeight = getActivity().getWindowManager().getDefaultDisplay().getHeight();

        gridWidth = deviceWidth * 2 / 15;
        timeWidth = deviceWidth - gridWidth * 7;
        gridHeight = getResources().getDimensionPixelOffset(R.dimen.syllabus_grid_height);

        showDate();

        showTime();

        showSyllabus();

        return view;
    }

    /**
     * 显示时间
     */
    private void showTime() {
        for (int i = 1; i <= 13; i++) {
            TextView timeTextView = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.week_grid, null, false);
            timeTextView.setText(i + "");
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, gridHeight);
            timeLinearLayout.addView(timeTextView, layoutParams);
        }

    }

    /**
     * 显示日期
     */
    private void showDate() {
        {
            TextView blankTextView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.week_grid, null, false);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, ViewGroup.LayoutParams.MATCH_PARENT
            );
            dateLinearLayout.addView(blankTextView, layoutParams);
        }
        for (int i = 0; i < 7; i++) {
            String[] weekString = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
            TextView weekTextView = (TextView) LayoutInflater.from(getActivity())
                    .inflate(R.layout.week_grid, null, false);
            weekTextView.setText(weekString[i]);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    gridWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            dateLinearLayout.addView(weekTextView, layoutParams);
        }
    }

    /**
     * 显示课程表
     */
    private void showSyllabus() {
        syllabusGridLayout.removeAllViews();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {

                if ((i + j) % 2 == 1) {
                    if (i % 2 == 1 && j == 0) ;
                    else continue;
                }

                GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.grid_background);

                StateListDrawable drawable = new StateListDrawable();

                int r = (int) (Math.random() * 256);
                int g = (int) (Math.random() * 256);
                int b = (int) (Math.random() * 256);
                shape.setColor(Color.argb(180, r, g, b));


                LinearLayout lessonLinearLayout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.lesson_grid, null, false);
                TextView lessonTextView = (TextView) lessonLinearLayout.findViewById(R.id.lessonTextView);
                lessonTextView.setText("第" + i + "天\n第" + j + "节课");
                lessonTextView.setWidth(gridWidth);
                lessonTextView.setBackgroundDrawable(shape);


                int span = 1;
                if (i % 2 == 1 && j == 0 || j == 12) {
                    lessonTextView.setHeight(gridHeight);
                } else {
                    lessonTextView.setHeight(gridHeight * 2);
                    span = 2;
                }

                GridLayout.Spec rowSpec = GridLayout.spec(j, span);
                GridLayout.Spec columnSpec = GridLayout.spec(i);
                syllabusGridLayout.addView(lessonLinearLayout, new GridLayout.LayoutParams(rowSpec, columnSpec));

            }

        }
        syllabusGridLayout.requestLayout();

    }

    @Override
    public void onResume() {
        super.onResume();

        /**
         * 圆形展开动画
         */
        syllabusGridLayout.post(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                    return;
                }
                for (int i = 0; i < syllabusGridLayout.getChildCount(); i++) {
                    View syllabusGridview = syllabusGridLayout.getChildAt(i);
                    Animator animator =  ViewAnimationUtils.createCircularReveal(
                            syllabusGridview,
                            syllabusGridview.getWidth() / 2,
                            syllabusGridview.getHeight() / 2,
                            0,
                            Math.max(syllabusGridview.getHeight(), syllabusGridview.getWidth()));
                    animator.setInterpolator(new AccelerateInterpolator());
                    animator.setDuration(500);
                    animator.start();
                }
            }
        });
    }
}
