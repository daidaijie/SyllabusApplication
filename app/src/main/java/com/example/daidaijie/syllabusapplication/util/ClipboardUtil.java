package com.example.daidaijie.syllabusapplication.util;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.example.daidaijie.syllabusapplication.App;

/**
 * Created by daidaijie on 2016/10/22.
 */

public class ClipboardUtil {

    public static void copyToClipboard(String text) {
        ClipboardManager myClipboard;
        myClipboard = (ClipboardManager)
                App.getContext().getSystemService(App.getContext().CLIPBOARD_SERVICE);
        ClipData clipData;
        clipData = ClipData.newPlainText("text"
                , text);
        myClipboard.setPrimaryClip(clipData);
    }
}
