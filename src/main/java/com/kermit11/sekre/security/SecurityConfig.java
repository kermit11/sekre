package com.kermit11.sekre.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests()
                .anyRequest()
                .permitAll()
                .and().oauth2Login()
                .and()
                .csrf().disable()
                ;
//        http.authorizeRequests()
//                .antMatchers("/", "/index.html")
//                .permitAll()
//                .anyRequest()
//                .authenticated();
    }


}
