package com.donation.common.utils;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void setUp(){
        databaseCleaner.execute();
    }
}
