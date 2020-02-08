package com.example.daggerexample.ui.auth;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.daggerexample.R;
import com.example.daggerexample.models.User;
import com.example.daggerexample.viewmodels.ViewModelsProvidersFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    public static final String TAG = AuthActivity.class.getSimpleName();

    private AuthViewModel viewModel;

    private EditText userId;

    @Inject
    Drawable logo;

    @Inject
    RequestManager glideInstance;

    @Inject
    ViewModelsProvidersFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        setLogo();
        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);

        userId = (EditText) findViewById(R.id.user_id_input);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        subscribeObserver();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setLogo() {
        glideInstance.
            load(logo)
        .into( (ImageView) findViewById(R.id.login_logo));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                onLoginButtonClick();
                break;

                default:
                    break;
        }

    }

    private void subscribeObserver() {
        viewModel.observeUsers().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    Toast.makeText(AuthActivity.this, "User Authenticated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AuthActivity.this, "User not Authenticated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onLoginButtonClick() {
        if (TextUtils.isEmpty(userId.getText().toString().trim())) {
            Toast.makeText(this, "Please provide valid user id", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.authenticateUserWithId(Integer.valueOf(userId.getText().toString().trim()));

    }
}
