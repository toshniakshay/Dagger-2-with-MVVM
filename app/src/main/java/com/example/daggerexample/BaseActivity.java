package com.example.daggerexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.example.daggerexample.models.User;
import com.example.daggerexample.ui.auth.AuthActivity;
import com.example.daggerexample.ui.auth.AuthResource;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Inject
    protected SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        subscribeObservers();
    }

    private void subscribeObservers() {
        sessionManager.getCachedUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                switch (userAuthResource.status) {
                    case ERROR:
                        Toast.makeText(BaseActivity.this, userAuthResource.message + "\n something went wrong", Toast.LENGTH_SHORT).show();
                        break;
                    case LOADING:
                        break;
                    case AUTHENTICATED:
                        Log.d(TAG, "onChanged: LOGIN SUCCESS");
                        break;
                    case NOT_AUTHENTICATED:
                        navigateToAuthActivity();
                        break;
                }
            }
        });
    }

    private void navigateToAuthActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
