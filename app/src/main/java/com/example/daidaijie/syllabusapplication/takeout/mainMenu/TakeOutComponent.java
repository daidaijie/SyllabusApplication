package com.example.daidaijie.syllabusapplication.takeout.mainMenu;

import com.example.daidaijie.syllabusapplication.AppComponent;
import com.example.daidaijie.syllabusapplication.PerActivity;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = AppComponent.class, modules = TakeOutModule.class)
public interface TakeOutComponent {

    void inject(TakeOutActivity takeOutActivity);
}
