package com.example.board.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:messages.properties")
public class PropertiesConfig  {

    private final Environment environment;

    public String getProperty(String key){
        return environment.getProperty(key);
    }

}
