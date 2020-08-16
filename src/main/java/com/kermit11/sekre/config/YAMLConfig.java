package com.kermit11.sekre.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig
{
    public String getMytest() {
        return mytest;
    }

    public void setMytest(String mytest) {
        this.mytest = mytest;
    }

    //TODO: get rid of this property
    private String mytest;


}
