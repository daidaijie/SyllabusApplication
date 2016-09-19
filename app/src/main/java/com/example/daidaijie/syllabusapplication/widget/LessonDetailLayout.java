package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;

/**
 * Created by daidaijie on 2016/7/27.
 */
public class LessonDetailLayout extends RelativeLayout {
    private ImageView mDetailItemImageView;
    private EditText mDetailItemEditText;
    private TextView mDetailDescTextView;

    private RelativeLayout mLayout;

    public LessonDetailLayout(Context context) {
        this(context, null);
    }

    public LessonDetailLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LessonDetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_lesson_detail
                , this, true);
        mDetailItemImageView = (ImageView) mLayout.findViewById(R.id.detailItemImageView);
        mDetailItemEditText = (EditText) mLayout.findViewById(R.id.detailItemEditText);
        mDetailDescTextView = (TextView) mLayout.findViewById(R.id.detailDescText);
        setEditEnable(false);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.LessonDetailLayout, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.LessonDetailLayout_editEnable:
                    setEditEnable(typedArray.getBoolean(attr, false));
                    break;
                case R.styleable.LessonDetailLayout_detailIcon:
                    setDetailIcon(typedArray.getResourceId(attr, 0));
                    break;
                case R.styleable.LessonDetailLayout_titltText:
                    setTitleText(typedArray.getString(attr));
                    break;
                case R.styleable.LessonDetailLayout_descText:
                    setDetailItemEditText(typedArray.getString(attr));
                    break;
                case R.styleable.LessonDetailLayout_titltTextColor:
                    setTitleTextColor(typedArray.getColor(attr,
                            getResources().getColor(R.color.defaultTextColor)));
                    break;
            }
        }
        if (typedArray!=null) {
            typedArray.recycle();
        }

    }

    public void setTitleText(String text) {
        mDetailItemEditText.setText(text);
    }

    public void setTitleText(Spannable text) {
        mDetailItemEditText.setText(text);
    }

    public void setTitleText(Spanned text) {
        mDetailItemEditText.setText(text);
    }


    public void setTitleTextColor(int color) {
        mDetailItemEditText.setTextColor(color);
    }
    public void setDetailIcon(int resId) {
        if (resId == 0) return;
        mDetailItemImageView.setImageResource(resId);
    }

    public void setEditEnable(boolean enable) {
        mDetailItemEditText.setFocusable(enable);
    }

    public void setDetailItemEditText(String text) {
        mDetailDescTextView.setText(text);
    }

    public void setDetailItemEditTextColor(int color) {
        mDetailDescTextView.setTextColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
