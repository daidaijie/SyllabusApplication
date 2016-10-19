package com.example.daidaijie.syllabusapplication.officeAutomation.oaDetail;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.officeAutomation.OAModelComponent;
import com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu.OAModule;
import com.example.daidaijie.syllabusapplication.officeAutomation.mainMenu.OfficeAutomationFragment;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/17.
 */

@PerFragment
@Component(dependencies = OAModelComponent.class, modules = OADetailModule.class)
public interface OADetailComponent {

    void inject(OADetailActivity oaDetailActivity);
}
