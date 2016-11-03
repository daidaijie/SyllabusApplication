package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.postDymatic;

import android.support.annotation.Nullable;

import org.joda.time.LocalDateTime;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/31.
 */

public interface IPostDymaticModel {

    LocalDateTime getStartTime();

    void setStartTime(LocalDateTime time);

    String getStartTimeString();

    LocalDateTime getEndTime();

    String getEndTimeString();

    void setEndTime(LocalDateTime time);

    interface OnPostCallBack {

        void onSuccess(String photoJson);

        void onFail(String msg);
    }

    List<String> getPhotoImgs();

    void postPhotoToBmob(OnPostCallBack onPostCallBack);

    Observable<Void> pushContent(@Nullable String photoListJson,
                                 String msg, String source, String url, String locate, boolean hasTime);

}
