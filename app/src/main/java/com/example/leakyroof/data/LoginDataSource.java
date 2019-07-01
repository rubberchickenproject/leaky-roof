package com.example.leakyroof.data;

import android.content.Intent;

import com.example.leakyroof.GameActivity;
import com.example.leakyroof.data.model.LoggedInUser;
import com.example.leakyroof.ui.login.LoginActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static Map<String, String> roster = new HashMap<>();

    public Result<LoggedInUser> login(String username, String password) {

        try {
            if (!roster.containsKey(username) || !roster.get(username).equals(password))
                return new Result.Error(new IOException("Incorrect username or password"));
            LoggedInUser returnUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.LoginSuccess<>(returnUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {}

    public Result<LoggedInUser> register(String username, String password) {

        try {
            // TODO: check that user and pass have the correct form
            if (roster.containsKey(username))
                return new Result.Error(new IOException("User already exists"));
            roster.put(username, password);
            LoggedInUser newUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.RegisterSuccess<>(newUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }
}
