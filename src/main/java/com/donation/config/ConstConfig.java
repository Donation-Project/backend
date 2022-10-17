package com.donation.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConstConfig {
    @Value("${basicImageProfile}")
    private String basicImageProfile;
}
