package com.example.daidaijie.syllabusapplication.exam;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/14.
 */

@PerModule
@Component(dependencies = UserComponent.class, modules = ExamModelModule.class)
public abstract class ExamModelComponent {

    public abstract IExamModel provideExamModel();

    private static ExamModelComponent INSTANCE;

    public static ExamModelComponent newInstance(AppComponent appComponent) {
        if (INSTANCE == null) {
            INSTANCE = DaggerExamModelComponent.builder()
                    .userComponent(UserComponent.buildInstance(appComponent))
                    .examModelModule(new ExamModelModule())
                    .build();
        }
        return INSTANCE;
    }

    public static ExamModelComponent getINSTANCE() {
        return INSTANCE;
    }

    public static void destroy() {
        INSTANCE = null;
    }

}
