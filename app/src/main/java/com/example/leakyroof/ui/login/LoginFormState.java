package com.example.leakyroof.ui.login;

import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * Data validation state of the login form.
 */
public class LoginFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    public LoginFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    public LoginFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof LoginFormState))
            return false;
        LoginFormState castObject = (LoginFormState) obj;
        return Objects.equals(castObject.getUsernameError(), getUsernameError()) &&
                Objects.equals(castObject.getPasswordError(), getPasswordError()) &&
                castObject.isDataValid() == isDataValid;
    }
}
