package com.example.daidaijie.syllabusapplication.stream;

import com.example.daidaijie.syllabusapplication.bean.StreamInfo;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/24.
 */

public interface IStreamModel {

    Observable<StreamInfo> getStreamInfo();
}
