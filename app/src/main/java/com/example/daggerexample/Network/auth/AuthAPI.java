package com.example.daggerexample.Network.auth;

import com.example.daggerexample.models.User;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthAPI {

    @GET("users/{id}")
    Flowable<User> getUser(@Path("id") int userId);

}
