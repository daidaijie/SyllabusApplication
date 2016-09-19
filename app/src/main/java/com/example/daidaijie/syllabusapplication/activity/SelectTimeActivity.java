package com.example.daidaijie.syllabusapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.model.AddLessonModel;
import com.example.daidaijie.syllabusapplication.model.ThemeModel;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SelectTimeActivity extends BaseActivity {

    private int timeWidth;
    private int gridWidth;
    private int gridHeight;

    public List<List<Boolean>> mSelectTimes;

    public static final String EXTRA_POSITION = "com.example.daidaijie.syllabusapplication.activity" +
            ".SelectTimeActivity.position";

    @BindView(R.id.titleTextView)
    TextView mTitleTextView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.dateLinearLayout)
    LinearLayout mDateLinearLayout;
    @BindView(R.id.timeLinearLayout)
    LinearLayout mTimeLinearLayout;
    @BindView(R.id.syllabusGridLayout)
    GridLayout mSyllabusGridLayout;
    @BindView(R.id.syllabusScrollView)
    ScrollView mSyllabusScrollView;
    @BindView(R.id.syllabusRootLayout)
    LinearLayout mSyllabusRootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mToolbar.setTitle("");
        setupToolbar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridWidth = deviceWidth * 2 / 15;
        timeWidth = deviceWidth - gridWidth * 7;
        gridHeight = getResources().getDimensionPixelOffset(R.dimen.syllabus_grid_height);

        mSelectTimes = AddLessonModel.getInstance().mTimes.get(getIntent().getIntExtra(EXTRA_POSITION, 0)).mSelectTimes;

        for (int i = 0; i < 7; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 13; j++) {
                sb.append(mSelectTimes.get(i).get(j) + ", ");
            }
            Logger.e("mSelectTimes " + i + ": " + sb.toString());
        }

        showDate();
        showTime();

        showSelectTime();

        setResult(203);
    }

    private void showSelectTime() {
        final GradientDrawable selectShape = (GradientDrawable) getResources().getDrawable(R.drawable.grid_background);
        final GradientDrawable unselectShape = (GradientDrawable) getResources().getDrawable(R.drawable.grid_background);

        selectShape.setColor(ColorUtils.setAlphaComponent(ThemeModel.getInstance().colorPrimary, 192));
        unselectShape.setColor(getResources().getColor(android.R.color.transparent));

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {
                LinearLayout lessonLinearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.select_lesson_grid, null, false);
                final TextView lessonTextView = (TextView) lessonLinearLayout.findViewById(R.id.lessonTextView);

                lessonTextView.setBackgroundDrawable(unselectShape);

                final int finalI = i;
                final int finalJ = j;
                lessonLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectTimes.get(finalI).get(finalJ)) {
                            mSelectTimes.get(finalI).set(finalJ, false);
                            lessonTextView.setBackgroundDrawable(unselectShape);
                        } else {
                            mSelectTimes.get(finalI).set(finalJ, true);
                            lessonTextView.setBackgroundDrawable(selectShape);
                        }
                    }
                });
                if (mSelectTimes.get(i).get(j)) {
                    lessonTextView.setBackgroundDrawable(selectShape);
                } else {
                    lessonTextView.setBackgroundDrawable(unselectShape);
                }
                lessonTextView.setWidth(gridWidth);
                lessonTextView.setHeight(gridHeight);
                GridLayout.Spec rowSpec = GridLayout.spec(j, 1);
                GridLayout.Spec columnSpec = GridLayout.spec(i);
                mSyllabusGridLayout.addView(lessonLinearLayout, new GridLayout.LayoutParams(rowSpec, columnSpec));
            }
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_time;
    }

    private void showDate() {
        {
            TextView blankTextView = (TextView) getLayoutInflater()
                    .inflate(R.layout.week_grid, null, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, ViewGroup.LayoutParams.MATCH_PARENT
            );
            mDateLinearLayout.addView(blankTextView, layoutParams);
        }
        for (int i = 0; i < 7; i++) {
            String[] weekString = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

            TextView weekTextView = (TextView) getLayoutInflater()
                    .inflate(R.layout.week_grid, null, false);
            weekTextView.setText(weekString[i]);
            if (i + 1 == 7) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    weekTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_week_end));
                } else {
                    weekTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_grid_week_end));
                }
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    gridWidth, ViewGroup.LayoutParams.MATCH_PARENT);
            mDateLinearLayout.addView(weekTextView, layoutParams);
        }
    }

    /**
     * 显示时间
     */
    private void showTime() {
        for (int i = 1; i <= 13; i++) {
            TextView timeTextView = (TextView) getLayoutInflater().inflate(R.layout.week_grid, null, false);
            timeTextView.setText(Syllabus.time2char(i) + "");
            if (i == 13) {
                timeTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_time_end));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, gridHeight);
            mTimeLinearLayout.addView(timeTextView, layoutParams);
        }

    }

    public static Intent getIntent(Context context, int position) {
        Intent intent = new Intent(context, SelectTimeActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        return intent;
    }

}
