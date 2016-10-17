package com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/17.
 */

@PerFragment
@Component(dependencies = OAModelComponent.class, modules = OAModule.class)
public interface OAComponent {

    void inject(OfficeAutomationFragment oAFragment);
}
