package com.example.daggerexample.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.daggerexample.Network.auth.AuthAPI;
import com.example.daggerexample.models.User;

import javax.inject.Inject;

import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private final AuthAPI authAPI;

    private MediatorLiveData<User> authUser = new MediatorLiveData<>();

    @Inject
    public AuthViewModel(AuthAPI authAPI) {
        this.authAPI = authAPI;

    }

    public void authenticateUserWithId(int id) {
        final LiveData<User> source = LiveDataReactiveStreams.fromPublisher(
            authAPI.getUser(id).
                subscribeOn(Schedulers.io())
        );

        authUser.addSource(source, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                authUser.setValue(user);
                authUser.removeSource(source);
            }
        });
    }

    public LiveData<User> observeUsers() {
        return authUser;
    }
}
