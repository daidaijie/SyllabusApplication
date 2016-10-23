package com.example.daidaijie.syllabusapplication.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.example.daidaijie.syllabusapplication.App;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Subscriber;

/**
 * Created by daidaijie on 2016/9/13.
 */
public class BitmapSaveUtil {

    public interface OnSaveFileCallBack {
        void onSuccess();

        void onFail(String msg);
    }


    public static void saveFile(final Bitmap bm, final String fileName, final int quality, final OnSaveFileCallBack onSaveFileCallBack) {

        RxPermissions.getInstance(App.getContext())
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        bm.recycle();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onSaveFileCallBack.onFail("授权失败");

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            String subFolder = FileUtil.get_app_folder(true) + App.FOIDER_NAME;
                            File folder = new File(subFolder);
                            if (!folder.exists()) {
                                folder.mkdirs();
                            }
                            File myCaptureFile = new File(subFolder, fileName);
                            if (!myCaptureFile.exists()) {
                                try {
                                    myCaptureFile.createNewFile();
                                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                                    bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
                                    bos.flush();
                                    bos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(App.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(myCaptureFile);
                            intent.setData(uri);
                            App.getContext().sendBroadcast(intent);
                            onSaveFileCallBack.onSuccess();
                        } else {
                            onSaveFileCallBack.onFail("截图失败");
                        }
                    }
                });

    }

    public static Bitmap getViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
}
