package com.kermit11.sekre.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("inMemAdminsRepo")
public class InMemAdminUsersDataAccessService implements AdminUsersDao
{
    //This is hard-coded just for the sake of having a working in-mem implementation, it is not intended to be used
    private static final List<String> allAdmins = List.of("kermit11@gmail.com");

    @Override
    public boolean isAdmin(String userID)
    {
        return allAdmins.contains(userID);
    }
}
