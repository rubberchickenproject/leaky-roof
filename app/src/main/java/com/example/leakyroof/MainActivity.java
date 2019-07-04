package com.example.leakyroof;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.leakyroof.data.LoginDataSource;
import com.example.leakyroof.ui.login.LoginViewModel;
import com.example.leakyroof.ui.login.LoginViewModelFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    protected LoginViewModel loginViewModel = null;
    protected String LOGIN_FILE_NAME = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onPause() {
        try {
            loginViewModel.writeUserInfo(LOGIN_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onPause();
    }
}
