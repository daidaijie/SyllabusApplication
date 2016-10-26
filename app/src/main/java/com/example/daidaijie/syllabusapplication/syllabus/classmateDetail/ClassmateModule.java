package com.example.daidaijie.syllabusapplication.syllabus.classmateDetail;

import com.example.daidaijie.syllabusapplication.di.qualifier.realm.UserRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.retrofitApi.LessonDetailApi;
import com.example.daidaijie.syllabusapplication.syllabus.ISyllabusModel;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/22.
 */

@Module
public class ClassmateModule {

    private long lessonID;

    private ClassmateContract.view mView;

    public ClassmateModule(ClassmateContract.view view, long lessonID) {
        mView = view;
        this.lessonID = lessonID;
    }

    @PerActivity
    @Provides
    long provideLessonID() {
        return lessonID;
    }

    @PerActivity
    @Provides
    ClassmateContract.view provideView() {
        return mView;
    }

    @PerActivity
    @Provides
    IClassmateModel provideModel(ISyllabusModel syllabusModel,
                                 @UserRealm Realm realm,
                                 @SchoolRetrofit Retrofit retrofit,
                                 long lessonID) {
        return new ClassmateModel(syllabusModel, realm, retrofit.create(LessonDetailApi.class), lessonID);
    }
}
