package com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.schoolDymatic.dymatic.SchoolDymaticModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/21.
 */

@PerFragment
@Component(dependencies = SchoolDymaticModelComponent.class, modules = SchoolDymaticModule.class)
public interface SchoolDymaticComponent {

    void inject(SchoolDymaticFragment schoolDymaticFragment);
}
