package com.example.daidaijie.syllabusapplication.schoolDymatic.circle.mainmenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDymatic.circle.StuCircleModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerFragment
@Component(dependencies = StuCircleModelComponent.class, modules = StuCircleModule.class)
public interface StuCircleComponent {

    void inject(StuCircleFragment stuCircleFragment);
}
