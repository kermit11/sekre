package com.kermit11.sekre.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("sqlAdminsRepo")
public class SQLAdminUsersDataAccessService implements AdminUsersDao
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean isAdmin(String userID)
    {
        String sqlStatement = "SELECT count(*)"
                + " FROM admins"
                + " WHERE user_id = ?";

        int count = jdbcTemplate.queryForObject(sqlStatement, new Object[]{userID}, Integer.TYPE);

        return count==1;
    }
}
