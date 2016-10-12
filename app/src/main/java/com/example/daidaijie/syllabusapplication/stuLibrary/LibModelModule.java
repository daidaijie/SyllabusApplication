package com.example.daidaijie.syllabusapplication.stuLibrary;

import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.bean.LibSearchBean;
import com.example.daidaijie.syllabusapplication.di.qualifier.retrofitQualifier.LibraryRetrofit;
import com.example.daidaijie.syllabusapplication.retrofitApi.LibraryApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by daidaijie on 2016/10/12.
 */

@Module
public class LibModelModule {

    private LibSearchBean mLibSearchBean;

    public LibModelModule(LibSearchBean searchBean) {
        this.mLibSearchBean = searchBean;
    }

    @Provides
    @PerModule
    LibSearchBean provideSearchBean() {
        return mLibSearchBean;
    }


    @Provides
    @PerModule
    ISTULibraryModel provideSTULibraryModel(@LibraryRetrofit Retrofit retrofit,
                                            LibSearchBean searchBean) {
        return new STULibraryModel(retrofit.create(LibraryApi.class), searchBean);
    }
}
