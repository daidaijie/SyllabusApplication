package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.example.daidaijie.syllabusapplication.R;

/**
 * Created by daidaijie on 2016/8/5.
 */
public class ItemCardLayout extends FrameLayout {

    private View mRootView;

    private ImageView mCardIconImageView;

    private TextView mCardDescTextView;

    private MaterialRippleLayout mRippleLayout;

    public ItemCardLayout(Context context) {
        this(context, null);
    }

    public ItemCardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = LayoutInflater.from(context).inflate(R.layout.item_layout_card
                , this, true);
        mCardIconImageView = (ImageView) mRootView.findViewById(R.id.cardIconImageView);
        mCardDescTextView = (TextView) mRootView.findViewById(R.id.cardDescTextView);
        mRippleLayout = (MaterialRippleLayout) mRootView.findViewById(R.id.rippleLayout);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ItemCardLayout, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ItemCardLayout_cardTitle:
                    setTitleText(typedArray.getString(attr));
                    break;
                case R.styleable.ItemCardLayout_cardIcon:
                    setCardIcon(typedArray.getResourceId(attr, 0));
                    break;
            }
        }
        if (typedArray != null) {
            typedArray.recycle();
        }
    }

    public void setTitleText(String text) {
        mCardDescTextView.setText(text);
    }

    public void setCardIcon(int resId) {
        if (resId == 0) return;
        mCardIconImageView.setImageResource(resId);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mRippleLayout.setOnClickListener(l);
    }
}
