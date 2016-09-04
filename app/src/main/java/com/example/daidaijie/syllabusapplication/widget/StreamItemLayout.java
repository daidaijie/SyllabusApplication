package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;

/**
 * Created by daidaijie on 2016/9/4.
 */
public class StreamItemLayout extends LinearLayout {

    private LinearLayout mLayout;

    private TextView mStateNameTextView;

    private TextView mStateInfoTextView;

    private View mDivLine;

    public StreamItemLayout(Context context) {
        this(context, null);
    }

    public StreamItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StreamItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_stream_info
                , this, true);

        mStateNameTextView = (TextView) mLayout.findViewById(R.id.stateNameTextView);
        mStateInfoTextView = (TextView) mLayout.findViewById(R.id.stateInfoTextView);
        mDivLine = mLayout.findViewById(R.id.div_line);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.StreamItemLayout, defStyleAttr, 0);

        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.StreamItemLayout_streamName:
                    setStreamName(typedArray.getString(attr));
                    break;
                case R.styleable.StreamItemLayout_streamInfo:
                    setStreamInfo(typedArray.getString(attr));
                    break;
                case R.styleable.StreamItemLayout_showLine:
                    setDivLineShow(typedArray.getBoolean(attr, false));
                    break;
            }
        }
        if (typedArray != null) {
            typedArray.recycle();
        }

    }


    public void setStreamName(String text) {
        mStateNameTextView.setText(text);
    }

    public void setStreamInfo(String text) {
        mStateInfoTextView.setText(text);
    }

    public void setDivLineShow(boolean isShow) {
        mDivLine.setVisibility(isShow ? VISIBLE : INVISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
