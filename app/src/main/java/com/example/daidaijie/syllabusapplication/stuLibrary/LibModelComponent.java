package com.example.daidaijie.syllabusapplication.stuLibrary;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.bean.LibSearchBean;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/9.
 */

@PerModule
@Component(dependencies = AppComponent.class, modules = LibModelModule.class)
public abstract class LibModelComponent {

    public abstract ISTULibraryModel getLibModel();

    private static LibModelComponent INSTANCE;

    public static LibModelComponent getInstance(AppComponent appComponent, LibSearchBean libSearchBean) {
        if (INSTANCE == null) {
            INSTANCE = DaggerLibModelComponent.builder()
                    .appComponent(appComponent)
                    .libModelModule(new LibModelModule(libSearchBean))
                    .build();
        }
        return INSTANCE;
    }

    public static LibModelComponent getInstance() {
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }

}
