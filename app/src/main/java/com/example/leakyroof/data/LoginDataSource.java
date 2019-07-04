package com.example.leakyroof.data;

import com.example.leakyroof.data.model.LoggedInUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private Map<String, String> roster = null;

    private void initializeIfNeeded(String pathname) {
        if (roster == null) {
            roster = new HashMap<>();
            try {
                File f = new File(pathname);
                f.createNewFile();
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f));
                roster = (Map) inputStream.readObject();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public Result<LoggedInUser> login(String username, String password, String pathname) {

        try {
            initializeIfNeeded(pathname);
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

    public Result<LoggedInUser> register(String username, String password, String pathname) {

        try {
            initializeIfNeeded(pathname);
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

    public void writeUserInfo(String pathname) throws IOException {
        initializeIfNeeded(pathname);
        FileOutputStream fileOutputStream = new FileOutputStream(pathname);
        ObjectOutputStream outputStream =
                new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(roster);
        outputStream.close();
    }
}
