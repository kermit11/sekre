package com.kermit11.sekre.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

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
        //We can't access super.requestCache, but it's the same one stored on the session
        DefaultSavedRequest savedRequest = (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        //If user was redirected to login page while attempting to submit a form, resume the form submission
        if (savedRequest!=null)
        {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }
        //Otherwise (=user clicked the "login" link directly), we still want to redirect to the original page (we stored it on the session), not to the default redirect target
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
