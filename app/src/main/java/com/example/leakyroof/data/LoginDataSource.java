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

    private Map<String, LoggedInUser> roster = null;

    private Exception initializeIfNeeded(String pathname) {
        // TODO: encrypt this!
        if (roster == null) {
            roster = new HashMap<>();
            try {
                File f = new File(pathname);
                f.createNewFile();
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(f));
                roster = (Map) inputStream.readObject();
                inputStream.close();
            } catch (IOException e) {
                return new IOException("Could not find roster file");
            } catch (ClassNotFoundException e) {
                return new ClassNotFoundException("File needs purging");
            }
        }
        return null;
    }

    public Result<LoggedInUser> login(
            String username, String password, String displayName, String pathname) {

        try {
            Exception initializeResult = initializeIfNeeded(pathname);
            if (!(initializeResult == null))
                return new Result.Error(initializeResult);
            if (!roster.containsKey(username) ||
                    !roster.get(username).getPassword().equals(password))
                return new Result.Error(new IOException("Incorrect username or password"));
            roster.put(username,
                    new LoggedInUser(username, displayName, password));
            return new Result.LoginSuccess<>(roster.get(username));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> register(
            String username, String password, String displayName, String pathname) {

        try {
            Exception initializeResult = initializeIfNeeded(pathname);
            if (!(initializeResult == null))
                return new Result.Error(initializeResult);
            // TODO: check that user and pass have the correct form
            if (roster.containsKey(username))
                return new Result.Error(new IOException("User already exists"));
            roster.put(username,
                    new LoggedInUser(username, displayName, password));
            return new Result.RegisterSuccess<>(roster.get(username));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public void writeUserInfo(String pathname) throws Exception {
        // TODO: make sure this is replacing the file's content each time
        Exception initializeResult = initializeIfNeeded(pathname);
        if (!(initializeResult == null))
            throw initializeResult;
        FileOutputStream fileOutputStream = new FileOutputStream(pathname);
        ObjectOutputStream outputStream =
                new ObjectOutputStream(fileOutputStream);
        outputStream.writeObject(roster);
        outputStream.close();
    }

    public void clearLoginInfo(String pathname) throws Exception {
        /** Resets all local and external records of user info, tread lightly
         *
         * Should only be callable from admin or programmatically
         */
        Exception initializeResult = initializeIfNeeded(pathname);
        if (!(initializeResult == null))
            throw initializeResult;
        roster = new HashMap<>();
        writeUserInfo(pathname);
    }
}
