package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.daidaijie.syllabusapplication.R;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by sunquan on 16/4/19.
 * 可这是是否居中显示，还是靠左对齐显示
 */
public class FlowLabelLayout extends ViewGroup {


    private int LayoutType=0;

    private static final int LayoutTYPE_CENTER = 0;
    private static final int LayoutTYPE_LEFT = 1;

    protected List<List<View>> mAllViews = new ArrayList<List<View>>();
    protected List<Integer> mLineHeight = new ArrayList<Integer>();

    public FlowLabelLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(attrs);
    }

    public FlowLabelLayout(Context context) {
        this(context, null);
    }

    public FlowLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        // 记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        // 得到内部元素的个数
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                if (i == cCount - 1) {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }
            // 测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            if(LayoutType==LayoutTYPE_LEFT){

                int realWidth =sizeWidth - getPaddingLeft() - getPaddingRight();

                int n=realWidth/child.getMeasuredWidth();

                int leftwidth =(realWidth-child.getMeasuredWidth()*n)/(n*2);

                lp.leftMargin=leftwidth;
                lp.rightMargin=leftwidth;
            }

            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            // 换行
            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else {// 未换行
                lineWidth += childWidth;// 叠加行宽
                lineHeight = Math.max(lineHeight, childHeight);// 得到当前行最大的高度
            }
            // 最后一个控件
            if (i == cCount - 1) {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(), modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()
        );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear();
        mLineHeight.clear();

        // 当前ViewGroup的宽度
        int width = getWidth();

        // 记录每一行的宽度与高度
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width) {

                // 记录LineHeight
                mLineHeight.add(lineHeight);
                // 记录当前行的Views
                mAllViews.add(lineViews);
                // 重置我们的行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                // 重置我们的View集合
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);


        int lineCount = mAllViews.size();

        List<String> childWidthList = new ArrayList<String>();

        for (int i = 0; i < lineCount; i++) {
            lineViews = mAllViews.get(i);
            int sum_w = 0;
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);

                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childWidth = child.getMeasuredWidth() + 2 * lp.leftMargin;
                sum_w = sum_w + childWidth;
            }

            childWidthList.add(sum_w + "");
        }

        int left=0;

        if(LayoutType==LayoutTYPE_CENTER){
            left = getPaddingLeft();
        }

        int top = getPaddingTop();


        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            if(LayoutType==LayoutTYPE_CENTER){
                left = (width - Integer.valueOf(childWidthList.get(i))) / 2;
            }else{
                left=0;
            }

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            top += lineHeight;
        }

    }

    private void init(AttributeSet attrs) {

        TypedArray ta = attrs == null ? null : getContext().obtainStyledAttributes(attrs, R.styleable.FlowLabelLayout);
        if (ta != null) {
            LayoutType = ta.getInteger(R.styleable.FlowLabelLayout_LayoutType,LayoutTYPE_CENTER);
            ta.recycle();
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


}
