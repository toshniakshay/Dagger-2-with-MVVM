package com.example.daggerexample.di;

import androidx.lifecycle.ViewModelProvider;

import com.example.daggerexample.viewmodels.ViewModelsProvidersFactory;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelsProvidersFactory factory);

}
