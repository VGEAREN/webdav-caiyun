package com.vgearen.webdavcaiyundrive.config;

import com.vgearen.webdavcaiyundrive.client.CaiyunDriverClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CaiyunProperties.class)
public class CaiyunDriverAutoConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaiyunDriverAutoConfig.class);

    @Autowired
    private CaiyunProperties caiyunProperties;

    @Bean
    public CaiyunDriverClient caiyunClient(ApplicationContext applicationContext) throws Exception {
        return new CaiyunDriverClient(caiyunProperties);
    }



}
