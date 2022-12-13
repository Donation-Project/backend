package com.donation.domain.inquiry.application;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.dto.InquiryUpdateReqDto;
import com.donation.domain.inquiry.entity.InquiryState;
import com.donation.domain.inquiry.repository.InquiryJdbcRepository;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.*;
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

    @BeforeEach
    void clear(){
        inquiryService.clear();
        inquiryJdbcRepository.clear();
    }
    @Test
    @DisplayName("문의글 작성 테스트")
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
    @DisplayName("문의글 전체 조회 테스트")
    void viewAll() {
        //given
        User save = userRepository.save(createUser("bbb@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        InquirySaveReqDto build3 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목3").content("내용3").user_id(save.getId()).build();
        inquiryService.createInquiry(build1);
        inquiryService.createInquiry(build2);
        inquiryService.createInquiry(build3);

        //when
        List<InquiryFindReqDto> find = inquiryService.viewAll();

        //then
        assertThat(find.get(0).getTitle()).isEqualTo("제목1");
        assertThat(find.size()).isEqualTo(3);

    }

    @Test
    @DisplayName("문의글 수정 테스트")
    void updateInquiry() {
        //given
        User save = userRepository.save(createUser("ccc@naver.com"));
        InquirySaveReqDto build = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("안녕").content("하세요").user_id(save.getId()).build();
        inquiryService.createInquiry(build);

        //when
        InquiryUpdateReqDto update = InquiryUpdateReqDto.builder().inquiry_id(save.getId()).title("수정된 제목").content("수정된 내용").build();
        inquiryService.updateInquiry(update);

        //then
        assertThat(update.getTitle()).isEqualTo("수정된 제목");
    }

    @Test
    void findByTitle() {
    }

    @Test
    void delete() {
    }
}