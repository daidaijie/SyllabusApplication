package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerActivity;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = TakeOutModelComponent.class, modules = TakeOutModule.class)
public interface TakeOutComponent {

    void inject(TakeOutActivity takeOutActivity);
}
