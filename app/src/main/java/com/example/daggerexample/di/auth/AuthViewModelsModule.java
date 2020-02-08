package com.example.daggerexample.di.auth;

import androidx.lifecycle.ViewModel;

import com.example.daggerexample.di.ViewModelKey;
import com.example.daggerexample.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindViewModel(AuthViewModel authViewModel);
}
