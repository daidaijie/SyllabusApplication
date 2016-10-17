package com.example.daidaijie.syllabusapplication.officeAutomation;

import com.example.daidaijie.syllabusapplication.bean.OABean;

import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/17.
 */

public interface IOAModel {

    Observable<List<OABean>> getLibrary(int position);

    Observable<List<OABean>> getLibraryFromMemory(int position);

    Observable<List<OABean>> getLibraryFromNet(int position);

    void setRead(OABean oaBean,boolean isRead);

    void clearRead();
}
