package com.example.daggerexample.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.daggerexample.Network.auth.AuthAPI;
import com.example.daggerexample.SessionManager;
import com.example.daggerexample.models.User;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    private final AuthAPI authAPI;

    private SessionManager sessionManager;

    @Inject
    public AuthViewModel(AuthAPI authAPI, SessionManager sessionManager) {
        this.authAPI = authAPI;
        this.sessionManager = sessionManager;

    }

    public void authenticateUserWithId(int id) {
        sessionManager.authenticateWithID(queryUserById(id));
    }

    private LiveData<AuthResource<User>> queryUserById(int id) {
        return LiveDataReactiveStreams.fromPublisher(
                authAPI.getUser(id).
                        onErrorReturn(new Function<Throwable, User>() {
                            @Override
                            public User apply(Throwable throwable) throws Exception {
                                User erroredUser = new User();
                                erroredUser.setId(-1);
                                return erroredUser;
                            }
                        }).
                        map(new Function<User, AuthResource<User>>() {
                            @Override
                            public AuthResource<User> apply(User user) throws Exception {
                                if (user.getId() == -1) {
                                    return AuthResource.error("User not authenticated", (User)null);
                                }
                                return AuthResource.authenticated(user);
                            }
                        }).
                        subscribeOn(Schedulers.io())
        );
    }

    public LiveData<AuthResource<User>> observeAuthState() {
        return sessionManager.getCachedUser();
    }
}
