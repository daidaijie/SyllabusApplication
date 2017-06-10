package com.example.daidaijie.syllabusapplication.syllabus.main.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.base.BaseFragment;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.bean.LessonID;
import com.example.daidaijie.syllabusapplication.bean.Syllabus;
import com.example.daidaijie.syllabusapplication.bean.SyllabusGrid;
import com.example.daidaijie.syllabusapplication.bean.TimeGrid;
import com.example.daidaijie.syllabusapplication.event.SaveSyllabusEvent;
import com.example.daidaijie.syllabusapplication.event.ShowTimeEvent;
import com.example.daidaijie.syllabusapplication.event.SyllabusEvent;
import com.example.daidaijie.syllabusapplication.syllabus.SyllabusComponent;
import com.example.daidaijie.syllabusapplication.syllabus.lessonDetail.LessonInfoActivity;
import com.example.daidaijie.syllabusapplication.syllabus.main.activity.SyllabusActivity;
import com.example.daidaijie.syllabusapplication.util.BitmapSaveUtil;
import com.example.daidaijie.syllabusapplication.util.LoggerUtil;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.SyllabusScrollView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import io.codetail.widget.RevealLinearLayout;
import io.realm.Realm;

public class SyllabusFragment extends BaseFragment implements SyllabusFragmentContract.view, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.timeLinearLayout)
    LinearLayout mTimeLinearLayout;
    @BindView(R.id.syllabusGridLayout)
    GridLayout mSyllabusGridLayout;
    @BindView(R.id.syllabusRootLayout)
    LinearLayout mSyllabusRootLayout;
    @BindView(R.id.dateLinearLayout)
    LinearLayout mDateLinearLayout;
    @BindView(R.id.syllabusScrollView)
    SyllabusScrollView mSyllabusScrollView;
    @BindView(R.id.syllabusRefreshLayout)
    SwipeRefreshLayout mSyllabusRefreshLayout;
    @BindView(R.id.detailTimeLinearLayout)
    LinearLayout mDetailTimeLinearLayout;
    TextView mTopBlankView;

    private static final String WEEK_DAY = SyllabusFragment.class.getCanonicalName() + ".WeekDate";

    private static final String IS_SWIPE_ENABLE = SyllabusFragment.class.getCanonicalName() + ".isSwipeEnable";

    private static final String IS_LOADED = SyllabusFragment.class.getCanonicalName() + ".isLoaded";

    @Inject
    SyllabusFragmentPresenter mSyllabusFragmentPresenter;


    //这里除了显示，在程序中皆从0开始，为第一周
    private int mWeek;

    private int timeWidth;
    private int detailTimeWidth;
    private int gridWidth;
    private int gridHeight;

    private boolean isLoaded;

    private SyllabusFragmentContract.OnSyllabusFragmentCallBack mOnSyllabusFragmentCallBack;

    private OnLessonClickListener mOnLessonClickListener;

    public static SyllabusFragment newInstance(int week) {
        SyllabusFragment fragment = new SyllabusFragment();
        Bundle args = new Bundle();
        args.putInt(WEEK_DAY, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeek = getArguments().getInt(WEEK_DAY);
        mOnSyllabusFragmentCallBack = (SyllabusFragmentContract.OnSyllabusFragmentCallBack) mActivity;
        mOnLessonClickListener = (OnLessonClickListener) mActivity;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        DaggerSyllabusFragmentComponent.builder()
                .syllabusComponent(SyllabusComponent.getINSTANCE())
                .syllabusFragmentModule(new SyllabusFragmentModule(this, mWeek))
                .build().inject(this);

        gridWidth = deviceWidth * 2 / 15;
        timeWidth = deviceWidth - gridWidth * 7;
        gridHeight = getResources().getDimensionPixelOffset(R.dimen.syllabus_grid_height);
        detailTimeWidth = getResources().getDimensionPixelOffset(R.dimen.detail_time_width);

        //解决滑动冲突
        mSyllabusScrollView.setSwipeRefreshLayout(mSyllabusRefreshLayout);

        setupSwipeRefreshLayout(mSyllabusRefreshLayout);
        mSyllabusRefreshLayout.setOnRefreshListener(this);

        showDate();
        showDetailTime();
        showTime();

        if (savedInstanceState != null) {
            isLoaded = savedInstanceState.getBoolean(IS_LOADED);
        } else {
            isLoaded = false;
        }

        mSyllabusFragmentPresenter.start();

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_syllabus;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SWIPE_ENABLE, mSyllabusRefreshLayout.isEnabled());
        outState.putBoolean(IS_LOADED, isLoaded);

        if (mWeek == 0) {
            LoggerUtil.e("load", "" + isLoaded);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mSyllabusRefreshLayout.setEnabled(savedInstanceState.getBoolean(IS_SWIPE_ENABLE));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUpdateSyllabus(SyllabusEvent event) {
        if (event.messageWeek != mWeek) {
            mSyllabusFragmentPresenter.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleUpdateSyllabus(ShowTimeEvent event) {
        if (event.messageWeek == mWeek) {
            if (event.isHide) {
                showDetailTime(false);
                return;
            }

            if (mDetailTimeLinearLayout.getVisibility() == View.VISIBLE) {
                showDetailTime(false);
            } else {
                showDetailTime(true);
            }
        }
    }

    private void showDetailTime(boolean isShow) {
        int visible = isShow ? View.VISIBLE : View.GONE;
        mDetailTimeLinearLayout.setVisibility(visible);
        mTopBlankView.setVisibility(visible);
    }

    private void showDate() {
        {
            mTopBlankView = (TextView) getActivity().getLayoutInflater()
                    .inflate(R.layout.week_grid, null, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    detailTimeWidth, ViewGroup.LayoutParams.MATCH_PARENT
            );
            mTopBlankView.setText("上课时间");
            mDateLinearLayout.addView(mTopBlankView, layoutParams);
            mTopBlankView.setVisibility(View.GONE);
        }

        {
            TextView blankTextView = (TextView) getActivity().getLayoutInflater()
                    .inflate(R.layout.week_grid, null, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, ViewGroup.LayoutParams.MATCH_PARENT
            );
            mDateLinearLayout.addView(blankTextView, layoutParams);
        }
        for (int i = 0; i < 7; i++) {
            String[] weekString = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

            TextView weekTextView = (TextView) getActivity().getLayoutInflater()
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
            TextView timeTextView = (TextView) LayoutInflater
                    .from(getActivity()).inflate(R.layout.week_grid, null, false);
            timeTextView.setText(Syllabus.time2char(i) + "");
            if (i == 13) {
                timeTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_time_end));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    timeWidth, gridHeight);
            mTimeLinearLayout.addView(timeTextView, layoutParams);
        }
    }

    /**
     * 显示具体时间
     */
    private void showDetailTime() {
        String[] detailTimeStrings = mActivity.getResources().getStringArray(R.array.detail_time);

        for (int i = 1; i <= 13; i++) {
            TextView timeTextView = (TextView) LayoutInflater
                    .from(getActivity()).inflate(R.layout.detail_time_grid, null, false);
            timeTextView.setText(detailTimeStrings[i - 1]);
            if (i == 13) {
                timeTextView.setBackground(getResources().getDrawable(R.drawable.bg_grid_time_end));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    detailTimeWidth, gridHeight);
            mDetailTimeLinearLayout.addView(timeTextView, layoutParams);
        }
    }


    @Override
    public void onLoadStart() {
        mOnSyllabusFragmentCallBack.onLoadStart();
    }

    @Override
    public void onLoadEnd(boolean success) {
        mOnSyllabusFragmentCallBack.onLoadEnd(success);
    }

    @Override
    public void showSyllabus(final Syllabus syllabus) {
        if (syllabus == null) {
            return;
        }

        mSyllabusGridLayout.removeAllViews();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 13; j++) {
                final SyllabusGrid syllabusGrid = syllabus.getSyllabusGrids(i, j);
                Lesson lesson = null;
                //在当前节点上找在本周上课的课程
                for (LessonID lessonID : syllabusGrid.getLessons()) {
                    Lesson tmpLesson = syllabus.getLessonByID(lessonID);

                    // 避免崩溃
                    if (Lesson.isNull(tmpLesson)) {
                        continue;
                    }

                    boolean flag = false;
                    for (TimeGrid timeGird : tmpLesson.getTimeGrids()) {
                        if (timeGird.getWeekDate() == i && timeGird.getTimeString().contains(Syllabus.time2char((j + 1)) + "")) {
                            long weekOfTime = timeGird.getWeekOfTime();
                            if (((weekOfTime >> mWeek) & 1) == 1) {
                                flag = true;
                            }
                        }
                    }
                    if (flag) {
                        lesson = tmpLesson;
                        break;
                    }
                }
                final RevealLinearLayout lessonLinearLayout = (RevealLinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.lesson_grid, null, false);
                MaterialRippleLayout lessonRippleLayout = (MaterialRippleLayout) lessonLinearLayout.findViewById(R.id.lessonRipple);
                TextView lessonTextView = (TextView) lessonLinearLayout.findViewById(R.id.lessonTextView);
                int span = 1;

                if (lesson != null) {
                    GradientDrawable shape = (GradientDrawable) getResources().getDrawable(R.drawable.grid_background);

                    shape.setColor(ColorUtils.setAlphaComponent(getResources().getColor(
                            lesson.getBgColor()), 192));
                    lessonTextView.setText(lesson.getTrueName() + "\n@" + lesson.getRoom());

                    lessonTextView.setBackgroundDrawable(shape);
                    for (int k = j + 1; k < 13; k++) {
                        SyllabusGrid nextSyllabusGrid = syllabus.getSyllabusGrids(i, k);
                        if (nextSyllabusGrid.getLessons().size() == 0) break;

                        Lesson nextLesson = null;
                        for (LessonID lessonID : nextSyllabusGrid.getLessons()) {
                            Lesson tmpLesson = syllabus.getLessonByID(lessonID);

                            // 避免崩溃
                            if (Lesson.isNull(tmpLesson)) {
                                continue;
                            }

                            boolean flag = false;
                            for (TimeGrid timeGird : tmpLesson.getTimeGrids()) {
                                if (timeGird.getWeekDate() == i && timeGird.getTimeString().contains(Syllabus.time2char((k + 1)) + "")) {
                                    long weekOfTime = timeGird.getWeekOfTime();
                                    if (((weekOfTime >> mWeek) & 1) == 1) {
                                        flag = true;
                                    }
                                }
                            }
                            if (flag) {
                                nextLesson = tmpLesson;
                                break;
                            }
                        }

                        if (nextLesson == null) break;

                        if (nextLesson.getId().equals(lesson.getId())) {
                            span++;
                        }
                    }
                    final Lesson finalLesson = Realm.getDefaultInstance().copyFromRealm(lesson);
                    final int finalSpan = span;
                    final int finalI = i;
                    final int finalJ = j;
                    lessonRippleLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnLessonClickListener.onLessonClick()) {
                                Intent intent = LessonInfoActivity.getIntent(mActivity, finalLesson.getLongID());
                                mActivity.startActivityForResult(intent, SyllabusActivity.REQUEST_LESSON_DETAIL);
                            }


                            /*final SyllabusActivity activity = (SyllabusActivity) getActivity();
                            if (!activity.isSingleLock()) {
                                activity.setSingleLock(true);
                                activity.showSelectWeekLayout(false);

                                Set<Long> lessonSet = new LinkedHashSet<>();
                                for (int k = 0; k < finalSpan; ++k) {
                                    SyllabusGrid tmpSyllabusGrid = syllabus.getSyllabusGrids(finalI, finalJ + k);
                                    lessonSet.addAll(tmpSyllabusGrid.getLessons());
                                }
                                List<Long> lessonIDs = new ArrayList<>();
                                lessonIDs.addAll(lessonSet);
                                if (lessonIDs.size() > 1) {
                                    final SelectLessonPopWindow selectLessonPopWindow = new SelectLessonPopWindow(activity, lessonIDs);
                                    selectLessonPopWindow.setBackgroundDrawable(new BitmapDrawable());
                                    selectLessonPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                                    selectLessonPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                                    selectLessonPopWindow.setOnItemClickListener(new SelectLessonPopWindow.OnItemClickListener() {
                                        @Override
                                        public void onClick(long lessonID) {
                                            Intent intent = LessonInfoActivity.getIntent(
                                                    getActivity(), lessonID
                                            );
                                            activity.startActivityForResult(intent, 200);
                                            selectLessonPopWindow.dismiss();

                                        }
                                    });
                                    selectLessonPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                                        @Override
                                        public void onDismiss() {
                                            activity.setSingleLock(false);
                                        }
                                    });
                                    selectLessonPopWindow.showAtLocation(mSyllabusRootLayout, Gravity.CENTER, 0, 0);
                                } else {
                                    Intent intent = LessonInfoActivity.getIntent(
                                            getActivity(), finalLesson.getLongID()
                                    );
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity,
                                                lessonLinearLayout, "lesson_grid");
                                        activity.startActivityForResult(intent, 200, options.toBundle());
                                    } else {
                                        activity.startActivityForResult(intent, 200);
                                    }
                                }

                            }*/

                        }
                    });

                } else {
                    lessonLinearLayout.setVisibility(View.INVISIBLE);
                    lessonRippleLayout.setEnabled(false);
                }

                lessonTextView.setWidth(gridWidth);
                lessonTextView.setHeight(gridHeight * span);
                GridLayout.Spec rowSpec = GridLayout.spec(j, span);
                GridLayout.Spec columnSpec = GridLayout.spec(i);
                mSyllabusGridLayout.addView(lessonLinearLayout, new GridLayout.LayoutParams(rowSpec, columnSpec));
                j += span - 1;
            }

        }
    }

    @Override
    public void showLoading(boolean isShow) {
        mSyllabusRefreshLayout.setRefreshing(isShow);
    }

    @Override
    public void showSuccessMessage(String msg) {
        SnackbarUtil.ShortSnackbar(
                mSyllabusRootLayout,
                msg,
                SnackbarUtil.Confirm
        ).show();
    }

    @Override
    public void loadData() {
        if (mWeek == 0 && !isLoaded) {
            mSyllabusRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSyllabusFragmentPresenter.loadData();
                    isLoaded = true;
                }
            });
        }
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.LongSnackbar(
                mSyllabusRootLayout,
                msg,
                SnackbarUtil.Alert
        ).setAction("再次同步", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSyllabusFragmentPresenter.loadData();
            }
        }).show();
    }


    @Override
    public void onRefresh() {
        showDetailTime(false);
        mSyllabusFragmentPresenter.loadData();
    }


    @Subscribe
    public void saveSyllaus(SaveSyllabusEvent saveSyllabusEvent) {
        if (saveSyllabusEvent.position == mWeek) {
            showDetailTime(false);
            Bitmap syllabusBitmap = BitmapSaveUtil.getViewBitmap(mSyllabusGridLayout);
            Bitmap timeBitmap = BitmapSaveUtil.getViewBitmap(mTimeLinearLayout);
            Bitmap dayBitmap = BitmapSaveUtil.getViewBitmap(mDateLinearLayout);

            mSyllabusFragmentPresenter.saveSyllabus(
                    syllabusBitmap, timeBitmap, dayBitmap);

        }
    }

    public interface OnLessonClickListener {
        boolean onLessonClick();
    }


}
