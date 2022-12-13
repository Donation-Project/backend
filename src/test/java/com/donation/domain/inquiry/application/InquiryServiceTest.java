package com.donation.domain.inquiry.application;

import com.donation.domain.inquiry.repository.InquiryJdbcRepository;
import com.donation.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class InquiryServiceTest {

    @Autowired
    InquiryJdbcRepository inquiryJdbcRepository;

    @Autowired
    InquiryService inquiryService;

    @Autowired
    UserRepository userRepository;

    @Test
    void createInquiry() {
    }

    @Test
    void viewAll() {
    }

    @Test
    void updateInquiry() {
    }

    @Test
    void findByTitle() {
    }

    @Test
    void delete() {
    }
}