package com.example.daggerexample.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.RequestManager;
import com.example.daggerexample.R;
import com.example.daggerexample.models.User;
import com.example.daggerexample.ui.main.MainActivity;
import com.example.daggerexample.viewmodels.ViewModelsProvidersFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    public static final String TAG = AuthActivity.class.getSimpleName();

    private AuthViewModel viewModel;

    private EditText userId;

    private ProgressBar progressBar;

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

        progressBar = findViewById(R.id.progress_bar);
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
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(AuthResource<User> userAuthResource) {
                if (userAuthResource != null) {
                    switch (userAuthResource.status) {
                        case ERROR:
                            showProgressbar(false);
                            Toast.makeText(AuthActivity.this, userAuthResource.message + "\n something went wrong", Toast.LENGTH_SHORT).show();
                            break;
                        case LOADING:
                            showProgressbar(true);
                            break;
                        case AUTHENTICATED:
                            showProgressbar(false);
                            Log.d(TAG, "onChanged: LOGIN SUCCESS");
                            navigateToDashboard();
                            break;
                        case NOT_AUTHENTICATED:
                            showProgressbar(false);
                            break;
                    }
                }
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressbar(boolean visibility) {
        progressBar.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    private void onLoginButtonClick() {
        if (TextUtils.isEmpty(userId.getText().toString().trim())) {
            Toast.makeText(this, "Please provide valid user id", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.authenticateUserWithId(Integer.valueOf(userId.getText().toString().trim()));

    }
}
