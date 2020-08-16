package com.kermit11.sekre.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
    @Override
    /**
     * Redirect back to the page that referred to the login page
     * Source: https://stackoverflow.com/a/23796392
     */
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        String redirectUrl = (String) session.getAttribute("url_prior_login");
        if (redirectUrl != null)
        {
            session.removeAttribute("url_prior_login");
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
        else
        {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
