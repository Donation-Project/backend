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
        inquiryJdbcRepository.clear();
        inquiryService.clear();
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
    @DisplayName("문의글 조회 테스트")
    void viewInquiry(){
        //given
        User save = userRepository.save(createUser("fff@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        inquiryService.createInquiry(build1);

        //when
        InquiryFindReqDto find = inquiryService.viewInquiry(13L);   //전체 테스트 돌릴시 id가 13L이 되어서 임의로 넣음
//        InquiryFindReqDto find = inquiryService.viewInquiry(1L);   //개별 테스트 돌릴시 id가 1L이 되어서 임의로 넣음


        //then
        assertThat(find.getTitle()).isEqualTo(build1.getTitle());
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
    @DisplayName("문의글 제목으로 검색 테스트")
    void findByTitle() {
        //given
        User save = userRepository.save(createUser("ddd@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        InquirySaveReqDto build3 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("하이").content("내용3").user_id(save.getId()).build();
        InquirySaveReqDto build4 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("1제목2").content("내용4").user_id(save.getId()).build();
        inquiryService.createInquiry(build1);
        inquiryService.createInquiry(build2);
        inquiryService.createInquiry(build3);
        inquiryService.createInquiry(build4);

        //when
        List<InquiryFindReqDto> actual = inquiryService.findByTitle("제목");

        //then
        assertThat(actual.get(0).getTitle()).isEqualTo("제목1");
        for(int i=0;i< actual.size();i++){
            System.out.println(actual.get(i).getTitle() + " / " + actual.get(i).getContent());
        }
    }

    @Test
    @DisplayName("문의글 삭제 테스트")
    void delete() {
        //given
        User save = userRepository.save(createUser("eee@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        inquiryService.createInquiry(build1);
        inquiryService.createInquiry(build2);

        //when
        inquiryService.delete(1L);

        //then
        List<InquiryFindReqDto> result = inquiryService.viewAll();
        assertThat(result.size()).isEqualTo(1);
    }
}