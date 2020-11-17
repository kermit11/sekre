package com.kermit11.sekre.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("props.logic")
public class LogicConfigProps
{
    public double getProbabilityForPollWithNoLikes() {
        return probabilityForPollWithNoLikes;
    }
    public void setProbabilityForPollWithNoLikes(double probabilityForPollWithNoLikes) {
        this.probabilityForPollWithNoLikes = probabilityForPollWithNoLikes;
    }
    private double probabilityForPollWithNoLikes;
}
