package com.example.spba.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ali-pay")
@Data
public class AlipayLocalConfig {

    private String merchantprivatekey;
    private String alipayPublicKey;
    private String appId;
    private String gatewayurl;

}
