package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.widget.bottomDialog.BaseBottomDialog;

import info.hoang8f.widget.FButton;


public class EditTextDialog extends BaseBottomDialog {

    private EditText mCommentEditText;

    private TextView mInfoTextView;

    private FButton mSendCommentButton;

    private String postUser;

    private int postID;

    public interface OnPostCommentCallBack {
        void onPostComment(int postID, String toAccount, String msg);
    }

    OnPostCommentCallBack mOnPostCommentCallBack;

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_edit_text;
    }

    @Override
    public void bindView(View v) {
        mSendCommentButton = (FButton) v.findViewById(R.id.sendCommentButton);
        mCommentEditText = (EditText) v.findViewById(R.id.commentEditText);
        mInfoTextView = (TextView) v.findViewById(R.id.infoTextView);
        mCommentEditText.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mCommentEditText, 0);
            }
        });
        mInfoTextView.setText("回复: " + postUser);
        mCommentEditText.addTextChangedListener(new MaxLinesTextWatcher(mCommentEditText, 16));

        mSendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mCommentEditText.getText().toString();
                if (msg.trim().isEmpty()) {
                    mCommentEditText.setError("评论不能为空!");
                } else {
                    if (mOnPostCommentCallBack != null) {
                        mOnPostCommentCallBack.onPostComment(postID, postUser, msg);
                        EditTextDialog.this.dismiss();
                    }
                }
            }
        });

    }

    public void setOnPostCommentCallBack(OnPostCommentCallBack onPostCommentCallBack) {
        mOnPostCommentCallBack = onPostCommentCallBack;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
