package com.example.daidaijie.syllabusapplication.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by daidaijie on 2016/9/29.
 */

public class CallPhoneDialog {

    public static AlertDialog createDialog(final Context context, final String[] phones) {
        final int[] select = {0};
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("请选择你要拨打的电话")
                .setSingleChoiceItems(phones, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select[0] = which;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPhone(context, phones[select[0]]);
                    }
                })
                .create();
        return dialog;
    }

    private static void callPhone(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
