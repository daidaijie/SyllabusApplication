package com.example.daidaijie.syllabusapplication.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by daidaijie on 2016/11/9.
 */

public class MaxLinesTextWatcher implements TextWatcher {


    EditText mEditText;

    int MAXLINES;

    public MaxLinesTextWatcher(EditText editText, int MAXLINES) {
        mEditText = editText;
        this.MAXLINES = MAXLINES;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int lines = mEditText.getLineCount();
// 限制最大输入行数
        if (lines > MAXLINES) {
            String str = s.toString();
            int cursorStart = mEditText.getSelectionStart();
            int cursorEnd = mEditText.getSelectionEnd();
            if (cursorStart == cursorEnd && cursorStart < str.length() && cursorStart >= 1) {
                str = str.substring(0, cursorStart - 1) + str.substring(cursorStart);
            } else {
                str = str.substring(0, s.length() - 1);
            }
// setText会触发afterTextChanged的递归
            mEditText.setText(str);
// setSelection用的索引不能使用str.length()否则会越界
            mEditText.setSelection(mEditText.getText().length());
        }
    }
}
