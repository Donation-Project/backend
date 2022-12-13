package com.donation.global.config;

import com.donation.domain.inquiry.repository.InquiryJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcConfig {

    private final DataSource dataSource;

    @Bean
    public InquiryJdbcRepository inquiryJdbcRepository(){
        return new InquiryJdbcRepository(dataSource);
    }
}
