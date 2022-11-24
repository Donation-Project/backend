package com.donation.infrastructure.support;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class AuthCodeGenerator {

    public String codeGenerator(){
        return String.valueOf(
                ThreadLocalRandom.current()
                        .nextInt(100_000,1_000_000)
        );
    }
}
