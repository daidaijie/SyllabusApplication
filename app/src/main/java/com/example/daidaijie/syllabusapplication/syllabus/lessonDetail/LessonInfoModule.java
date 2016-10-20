package com.example.daidaijie.syllabusapplication.syllabus.lessonDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by daidaijie on 2016/10/20.
 */

@Module
public class LessonInfoModule {

    private long id;

    private LessonInfoContract.view mView;

    public LessonInfoModule(long id, LessonInfoContract.view view) {
        this.id = id;
        mView = view;
    }

    @PerActivity
    @Provides
    long provideId() {
        return id;
    }

    @PerActivity
    @Provides
    LessonInfoContract.view provideView() {
        return mView;
    }
}
