package com.example.daidaijie.syllabusapplication.grade;

import android.app.Activity;

import com.example.daidaijie.syllabusapplication.ILoginModel;
import com.example.daidaijie.syllabusapplication.di.qualifier.realm.DefaultRealm;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.SchoolRetrofit;
import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.main.BannerModel;
import com.example.daidaijie.syllabusapplication.main.IBannerModel;
import com.example.daidaijie.syllabusapplication.main.MainContract;
import com.example.daidaijie.syllabusapplication.retrofitApi.BannerApi;
import com.example.daidaijie.syllabusapplication.widget.SelectSemesterBuilder;
import com.example.daidaijie.syllabusapplication.widget.picker.LinkagePicker;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/8.
 */

@Module
public class GradeModule {

    private final MainContract.view mView;

    private Activity mActivity;


    public GradeModule(MainContract.view view, Activity activity) {
        mView = view;
        mActivity = activity;
    }

    @Provides
    @PerActivity
    MainContract.view provideView() {
        return mView;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    IBannerModel provideBannerModel(@DefaultRealm Realm realm,
                                    @SchoolRetrofit Retrofit retrofit) {
        return new BannerModel(realm, retrofit.create(BannerApi.class));
    }

    @Provides
    @PerActivity
    LinkagePicker provideSemesterPicker(Activity activity, ILoginModel mLoginModel) {
        return SelectSemesterBuilder.newSelectSemesterPicker(activity, mLoginModel.getCurrentSemester());
    }
}
