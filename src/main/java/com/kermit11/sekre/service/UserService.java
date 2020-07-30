package com.kermit11.sekre.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService
{
    public String getCurrentUserName()
    {
        Optional<Object> opt = Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return opt
                .filter(u -> u instanceof OidcUser)
                .map(u -> ((OidcUser)u).getFullName())
                .orElse("אורח");
    }

    public String getCurrentUserEmail()
    {
        Optional<Object> opt = Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return opt
                .filter(u -> u instanceof OidcUser)
                .map(u -> ((OidcUser)u).getEmail())
                .orElse("guest");
    }

}
