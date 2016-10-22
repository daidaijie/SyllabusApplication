package com.example.daidaijie.syllabusapplication.stuLibrary.bookDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/22.
 */

@PerActivity
@Component(dependencies = LibModelComponent.class, modules = BookDetailModule.class)
public interface BookDetailComponent {

    void inject(BookDetailActivity bookDetailActivity);
}
