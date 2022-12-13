package com.donation.domain.inquiry.application;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.entity.InquiryState;
import com.donation.domain.inquiry.repository.InquiryJdbcRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class InquiryServiceTest {

    @Autowired
    InquiryJdbcRepository inquiryJdbcRepository;

    @Autowired
    InquiryService inquiryService;

    @Autowired
    UserRepository userRepository;

    @Test
    void createInquiry() {
        //given
        User save = userRepository.save(createUser("aaa@naver.com"));
        InquirySaveReqDto build = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();

        //when
        inquiryService.createInquiry(build);
        inquiryService.createInquiry(build2);

        //then
        List<InquiryFindReqDto> find = inquiryService.viewAll();
        assertThat(find.get(0).getTitle()).isEqualTo("제목1");
        assertThat(find.get(0).getContent()).isEqualTo("내용1");

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