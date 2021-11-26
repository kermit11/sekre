package com.kermit11.sekre.service;

import com.kermit11.sekre.dao.AdminUsersDao;
import com.kermit11.sekre.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    private final AdminUsersDao adminUsersDao;

    public static final User GUEST_USER = new User("guest", "אורח", true);

    @Autowired
    public UserService(@Qualifier("sqlAdminsRepo") AdminUsersDao adminUsersDao)
    {
        this.adminUsersDao = adminUsersDao;
    }

    public User getCurrent()
    {
        Optional<Object> opt = Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return opt
                .filter(u -> u instanceof OidcUser)
                .map(u -> {
                    String userID = ((OidcUser) u).getEmail();
                    String userName = ((OidcUser) u).getFullName();
                    boolean isAdmin = adminUsersDao.isAdmin(userID);
                    return new User(userID, userName, isAdmin);
                })
                .orElse(GUEST_USER);
    }

}
