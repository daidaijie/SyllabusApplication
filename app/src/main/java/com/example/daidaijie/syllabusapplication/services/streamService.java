package com.example.daidaijie.syllabusapplication.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

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
        if (mStreamInfo != null) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext());
            double progress = (mStreamInfo.getNowByte() / mStreamInfo.getAllByte()) * 100;

            builder.setProgress(100, (int) progress, false)
                    .setContentTitle(mStreamInfo.getName() + "的流量使用情况")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("已用" + mStreamInfo.getNowStream() + ",总共" + mStreamInfo
                            .getAllStream() + "。状态" + mStreamInfo.getState())
                    .setWhen(System.currentTimeMillis());
            Notification notification = builder.build();

            startForeground(101, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
