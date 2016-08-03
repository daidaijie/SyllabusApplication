package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.daidaijie.syllabusapplication.R;

/**
 * Created by daidaijie on 2016/8/3.
 */
public class LoadingDialogBuiler {
    public static AlertDialog getLoadingDialog(Context context,int barColor) {
        RelativeLayout loadingDialogLayout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.dialog_loading, null, false);
        ProgressBar loadingProgressBar = (ProgressBar) loadingDialogLayout
                .findViewById(R.id.loadingProgressBar);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(loadingDialogLayout)
                .create();
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(
                barColor,
                android.graphics.PorterDuff.Mode.SRC_IN);
        dialog.setCancelable(false);
        return dialog;
    }
}
