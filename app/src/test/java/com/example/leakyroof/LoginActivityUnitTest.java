package com.example.leakyroof;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.leakyroof.ui.login.LoggedInUserView;
import com.example.leakyroof.ui.login.LoginActivity;
import com.example.leakyroof.ui.login.LoginFormState;
import com.example.leakyroof.ui.login.LoginResult;
import com.example.leakyroof.ui.login.LoginViewModel;
import com.example.leakyroof.ui.login.LoginViewModelFactory;
import androidx.test.internal.platform.app.ActivityInvoker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Local unit test for login functionality, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class LoginActivityUnitTest {

    private LoginActivity MOCK_ACTIVITY;
    private LoginViewModel LOGIN_VIEW_MODEL;
    private String VALID_USERNAME;
    private String INVALID_USERNAME_EMPTY;
    private String INVALID_USERNAME_NO_DOMAIN;
    private String WRONG_USERNAME;
    private String VALID_PASSWORD;
    private String WRONG_PASSWORD;
    private String INVALID_PASSWORD_SHORT;
    private String PATH_NAME;

    @Before
    public void setUp() {
        MOCK_ACTIVITY = Robolectric.buildActivity(LoginActivity.class).create().get();
        LOGIN_VIEW_MODEL = ViewModelProviders.of(MOCK_ACTIVITY, new LoginViewModelFactory())
                        .get(LoginViewModel.class);
        VALID_USERNAME = "name@example.com";
        INVALID_USERNAME_EMPTY = " ";
        INVALID_USERNAME_NO_DOMAIN = "name@example";
        WRONG_USERNAME = "wrongname@example.com";

        VALID_PASSWORD = "password";
        WRONG_PASSWORD = "wrong_password";
        INVALID_PASSWORD_SHORT = "pass";

        PATH_NAME = "test_roster";
    }

    @After
    public void tearDown() {
        try {
            LOGIN_VIEW_MODEL.clearLoginInfo(PATH_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dataChanged_valid() {
        LOGIN_VIEW_MODEL.loginDataChanged(VALID_USERNAME, VALID_PASSWORD);
        LoginFormState actualResult = LOGIN_VIEW_MODEL.getLoginFormState().getValue();
        LoginFormState expectedResult = new LoginFormState(true);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void dataChanged_email_invalid_no_at() {
        LOGIN_VIEW_MODEL.loginDataChanged(INVALID_USERNAME_EMPTY, VALID_PASSWORD);
        LoginFormState actualResult = LOGIN_VIEW_MODEL.getLoginFormState().getValue();
        LoginFormState expectedResult = new LoginFormState(R.string.invalid_username, null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void dataChanged_email_invalid_no_domain() {
        LOGIN_VIEW_MODEL.loginDataChanged(INVALID_USERNAME_NO_DOMAIN, VALID_PASSWORD);
        LoginFormState actualResult = LOGIN_VIEW_MODEL.getLoginFormState().getValue();
        LoginFormState expectedResult = new LoginFormState(R.string.invalid_username, null);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void dataChanged_password_invalid() {
        LOGIN_VIEW_MODEL.loginDataChanged(VALID_USERNAME, INVALID_PASSWORD_SHORT);
        LoginFormState actualResult = LOGIN_VIEW_MODEL.getLoginFormState().getValue();
        LoginFormState expectedResult = new LoginFormState(null, R.string.invalid_password);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void registration_successful() {
        LOGIN_VIEW_MODEL.register(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);
        LoginResult actualResult = LOGIN_VIEW_MODEL.getLoginResult().getValue();
        LoginResult expectedResult = new LoginResult(new LoggedInUserView(VALID_USERNAME));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void registration_username_taken() {
        LOGIN_VIEW_MODEL.register(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);

        LOGIN_VIEW_MODEL.register(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);
        LoginResult actualResult = LOGIN_VIEW_MODEL.getLoginResult().getValue();
        LoginResult expectedResult = new LoginResult(R.string.register_failed);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void login_successful() {
        LOGIN_VIEW_MODEL.register(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);

        LOGIN_VIEW_MODEL.login(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);
        LoginResult actualResult = LOGIN_VIEW_MODEL.getLoginResult().getValue();
        LoginResult expectedResult = new LoginResult(new LoggedInUserView(VALID_USERNAME));
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void login_wrong_password() {
        LOGIN_VIEW_MODEL.register(VALID_USERNAME, VALID_PASSWORD, PATH_NAME);

        LOGIN_VIEW_MODEL.login(VALID_USERNAME, WRONG_PASSWORD, PATH_NAME);
        LoginResult actualResult = LOGIN_VIEW_MODEL.getLoginResult().getValue();
        LoginResult expectedResult = new LoginResult(R.string.login_failed);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void login_user_nonexistent() {
        LOGIN_VIEW_MODEL.login(WRONG_USERNAME, WRONG_PASSWORD, PATH_NAME);
        LoginResult actualResult = LOGIN_VIEW_MODEL.getLoginResult().getValue();
        LoginResult expectedResult = new LoginResult(R.string.login_failed);
        assertEquals(expectedResult, actualResult);
    }
}