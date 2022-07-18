package com.github.dmgiangi.brewerhub.services;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.Properties;

@Getter
@Configuration
public class DatabasePropertyLoader extends Properties{
    final static Logger logger = LoggerFactory.getLogger(DatabasePropertyLoader.class);

    @Value("${database.type}")
    private String type;
    @Value("${database.host}")
    private String host;
    @Value("${database.name}")
    private String name;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;
    @Value("${database.port}")
    private String port;
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
