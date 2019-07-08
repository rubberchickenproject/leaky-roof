package com.example.leakyroof.ui.login;

import android.support.annotation.Nullable;

import java.util.Objects;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof LoggedInUserView))
            return false;
        LoggedInUserView castObject = (LoggedInUserView) obj;
        return Objects.equals(castObject.getDisplayName(), getDisplayName());
    }
}
