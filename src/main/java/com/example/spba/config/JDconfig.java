package com.example.spba.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "local.jd")
public class JDconfig {
    private String appSecret;
    private String appKey;
}
