package com.example.daggerexample.di;

import com.example.daggerexample.di.auth.AuthModule;
import com.example.daggerexample.di.auth.AuthViewModelsModule;
import com.example.daggerexample.ui.auth.AuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(
        modules = {AuthViewModelsModule.class, AuthModule.class}
    )
    abstract AuthActivity contributeAuthActivity();
}
