package com.example.daidaijie.syllabusapplication.widget;

import android.view.View;
import android.widget.RelativeLayout;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.widget.bottomDialog.BaseBottomDialog;


public class ShareWXDialog extends BaseBottomDialog implements View.OnClickListener {

    RelativeLayout mShareCircleLayout;
    RelativeLayout mShareFriendLayout;
    RelativeLayout mShareClipBoardLayout;

    @Override
    public void onClick(View v) {
        if (mOnShareSelectCallBack != null) {
            if (v.getId() == R.id.shareFriendLayout) {
                mOnShareSelectCallBack.onShareSelect(0);
            } else if (v.getId() == R.id.shareCircleLayout) {
                mOnShareSelectCallBack.onShareSelect(1);
            } else {
                mOnShareSelectCallBack.onShareSelect(2);
            }
        }
        this.dismiss();
    }

    public interface OnShareSelectCallBack {
        void onShareSelect(int position);
    }

    OnShareSelectCallBack mOnShareSelectCallBack;

    public OnShareSelectCallBack getOnShareSelectCallBack() {
        return mOnShareSelectCallBack;
    }

    public void setOnShareSelectCallBack(OnShareSelectCallBack onShareSelectCallBack) {
        mOnShareSelectCallBack = onShareSelectCallBack;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.dialog_share_wx;
    }

    @Override
    public void bindView(View v) {
        mShareCircleLayout = (RelativeLayout) v.findViewById(R.id.shareCircleLayout);
        mShareFriendLayout = (RelativeLayout) v.findViewById(R.id.shareFriendLayout);
        mShareClipBoardLayout = (RelativeLayout) v.findViewById(R.id.shareClipBoardLayout);

        mShareCircleLayout.setOnClickListener(this);
        mShareFriendLayout.setOnClickListener(this);
        mShareClipBoardLayout.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
