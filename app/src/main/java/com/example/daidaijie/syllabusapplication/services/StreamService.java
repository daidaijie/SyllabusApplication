package com.example.daidaijie.syllabusapplication.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.bean.StreamInfo;

public class StreamService extends Service {

    public static final String EXTRA_STREAM_INFO = "com.example.daidaijie.syllabusapplication" +
            ".services.StreamService.StreamInfo";

    public static Intent getIntent(Context context, StreamInfo streamInfo) {
        Intent intent = new Intent(context, StreamService.class);
        intent.putExtra(EXTRA_STREAM_INFO, streamInfo);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StreamInfo mStreamInfo = (StreamInfo) intent.getSerializableExtra(EXTRA_STREAM_INFO);
        int type = mStreamInfo.getType();

        if (type == StreamInfo.TYPE_SUCCESS && mStreamInfo != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
            double progress = (mStreamInfo.getNowByte() / mStreamInfo.getAllByte()) * 100;

            builder.setProgress(100, (int) progress, false)
                    .setContentTitle(mStreamInfo.getName() + "的流量使用情况: " +
                            String.format("%.2f%%", progress))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("已用" + mStreamInfo.getNowStream() + ",总共" + mStreamInfo
                            .getAllStream() + "。状态" + mStreamInfo.getState())
                    .setWhen(System.currentTimeMillis());
            Notification notification = builder.build();

            startForeground(101, notification);
            Log.e("ServiceTest", "已连接");
        }
        if (type == StreamInfo.TYPE_UN_CONNECT) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());

            builder.setContentTitle("网络状态")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("没连接到校园网")
                    .setWhen(System.currentTimeMillis());
            Notification notification = builder.build();

            startForeground(101, notification);
            Log.e("ServiceTest", "已断开");
        }
        if (type == StreamInfo.TYPE_LOGOUT) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());

            builder.setContentTitle("网络状态")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("没登录校园网流量验证")
                    .setWhen(System.currentTimeMillis());
            Notification notification = builder.build();

            startForeground(101, notification);
            Log.e("ServiceTest", "无登录");

        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
