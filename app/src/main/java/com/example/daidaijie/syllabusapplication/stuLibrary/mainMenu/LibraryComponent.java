package com.example.daidaijie.syllabusapplication.stuLibrary.mainMenu;

import com.example.daidaijie.syllabusapplication.di.scope.PerFragment;
import com.example.daidaijie.syllabusapplication.stuLibrary.LibModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerFragment
@Component(dependencies = LibModelComponent.class, modules = LibraryModule.class)
public interface LibraryComponent {

    void inject(LibraryFragment libraryFragment);
}
