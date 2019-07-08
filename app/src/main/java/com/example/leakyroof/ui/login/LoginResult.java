package com.example.leakyroof.ui.login;

import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    public LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof LoginResult))
            return false;
        LoginResult castObject = (LoginResult) obj;
        return Objects.equals(castObject.getError(), getError()) &&
                Objects.equals(castObject.getSuccess(), getSuccess());
    }
}
