package com.example.daidaijie.syllabusapplication.stuLibrary;

import com.example.daidaijie.syllabusapplication.bean.LibraryBean;

import java.io.UnsupportedEncodingException;
import java.util.List;

import rx.Observable;

/**
 * Created by daidaijie on 2016/10/12.
 */

public interface ISTULibraryModel {

    Observable<List<LibraryBean>> getLibrary(int position);

    Observable<List<LibraryBean>> getLibraryFromMemory(int position);

    Observable<List<LibraryBean>> getLibraryFromNet(int position);

    LibraryBean getLibraryBean(int position, int subPosition);
}
