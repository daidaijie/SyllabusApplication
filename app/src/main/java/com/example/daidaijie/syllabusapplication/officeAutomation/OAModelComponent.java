package com.example.daidaijie.syllabusapplication.officeAutomation;

import com.example.daidaijie.syllabusapplication.bean.OASearchBean;
import com.example.daidaijie.syllabusapplication.di.scope.PerModule;
import com.example.daidaijie.syllabusapplication.user.UserComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/17.
 */

@PerModule
@Component(dependencies = UserComponent.class, modules = OAModelModule.class)
public abstract class OAModelComponent {

    private static OAModelComponent INSTANCE;

    public static OAModelComponent newInstance(UserComponent userComponent,
                                        OASearchBean oaSearchBean) {
        if (INSTANCE == null) {
            INSTANCE = DaggerOAModelComponent.builder()
                    .userComponent(userComponent)
                    .oAModelModule(new OAModelModule(oaSearchBean))
                    .build();
        }
        return INSTANCE;
    }

    public static OAModelComponent getINSTANCE() {
        return INSTANCE;
    }

    public static void destory() {
        INSTANCE = null;
    }

    public abstract IOAModel getOAModel();
}
