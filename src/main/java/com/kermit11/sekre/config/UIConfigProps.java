package com.kermit11.sekre.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("props.ui")
public class UIConfigProps
{
    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }
    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
    private Integer defaultPageSize;
}
