package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.Lesson;
import com.example.daidaijie.syllabusapplication.model.LessonModel;
import com.example.daidaijie.syllabusapplication.util.DensityUtil;

import java.util.List;

/**
 * Created by daidaijie on 2016/9/23.
 */

public class SelectLessonPopWindow extends PopupWindow {


    private View mView;

    private List<Long> mLessons;

    private ViewPager mLessonPager;

    private Context mContext;

    private MyVpAdapter mMyVpAdapter;

    OnItemClickListener mOnItemClickListener;

    public SelectLessonPopWindow(Context context, List<Long> lesson) {
        super(context);
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mView = inflater.inflate(R.layout.pop_select_lesson, null);
        mLessonPager = (ViewPager) mView.findViewById(R.id.selectLessonPager);
        mLessons = lesson;
        mMyVpAdapter = new MyVpAdapter();
        mLessonPager.setPageMargin(DensityUtil.dip2px(mContext, 16));
        mLessonPager.setAdapter(mMyVpAdapter);
        mLessonPager.setOffscreenPageLimit(3);
        mLessonPager.setPageTransformer(false, new ScaleTransformer());
        setContentView(mView);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
    }

    public interface OnItemClickListener {
        void onClick(long lessonID);
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public class MyVpAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mLessons.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final Lesson lesson = LessonModel.getInstance().getLesson(mLessons.get(position));

//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_select_lesson, null);
            CardView cardView = new CardView(mContext);
            cardView.setCardBackgroundColor(mContext.getResources().getColor(lesson.getBgColor()));
            int padding = DensityUtil.dip2px(mContext, 4);
            cardView.setRadius(padding);
            cardView.setCardElevation(padding);
            padding = DensityUtil.dip2px(mContext, 16);
            cardView.setContentPadding(padding, padding, padding, padding);
            TextView textView = new TextView(mContext);
            textView.setTextSize(16);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mContext.getResources().getColor(R.color.material_white));
            textView.setText(lesson.getTrueName() + "\n@" + lesson.getRoom());

            cardView.addView(textView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(lesson.getLongID());
                    }
                }
            });

            container.addView(cardView);
            return cardView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public class ScaleTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.70f;
        private static final float MIN_ALPHA = 0.7f;

        @Override
        public void transformPage(View page, float position) {
            if (position < -1 || position > 1) {
                page.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);
            } else if (position <= 1) { // [-1,1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                if (position < 0) {
                    float scaleX = 1 + 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                } else {
                    float scaleX = 1 - 0.3f * position;
                    page.setScaleX(scaleX);
                    page.setScaleY(scaleX);
                }
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }
    }

}
