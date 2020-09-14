package com.kermit11.sekre.model;

import org.springframework.lang.NonNull;

public class User
{
    @NonNull
    private final String userID;
    public String getUserID() {
        return userID;
    }

    private final String userName;
    public String getUserName() {
        return userName;
    }

    private boolean isAdmin;
    public boolean isAdmin() {
        return isAdmin;
    }

    public User(String userID, String userName, boolean isAdmin)
    {
        this.userID = userID;
        this.isAdmin = isAdmin;
        this.userName = userName;
    }
}
