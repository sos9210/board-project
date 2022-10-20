package com.example.board.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:/application.properties")
class PropertiesConfigTest {

    @Autowired
    Environment environment;

    @Test
    void 프로퍼티_값을_가져온다() {
        String property = environment.getProperty("test.env");
        Assertions.assertEquals("confirm",property);
    }
}