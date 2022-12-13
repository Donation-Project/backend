package com.donation.domain.inquiry.repository;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.dto.InquiryUpdateReqDto;
import com.donation.domain.inquiry.entity.Inquiry;
import com.donation.domain.inquiry.entity.InquiryState;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class InquiryJdbcRepositoryTest {

    @Autowired
    InquiryJdbcRepository inquiryJdbcRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void clear(){
        inquiryJdbcRepository.clear();
    }

    @Test
    @DisplayName("저장 테스트")
    void save(){
        //given
        User save = userRepository.save(createUser("aaa@naver.com"));
        InquirySaveReqDto build = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();

        //when
        inquiryJdbcRepository.save(build);
        inquiryJdbcRepository.save(build2);

        //then
        List<InquiryFindReqDto> find = inquiryJdbcRepository.findAll();
        assertThat(find.get(0).getTitle()).isEqualTo("제목1");
        assertThat(find.get(0).getContent()).isEqualTo("내용1");
    }

    @Test
    @DisplayName("아이디로 검색 테스트")
    void findById(){
        //given
        User save = userRepository.save(createUser("fff@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        inquiryJdbcRepository.save(build1);
        inquiryJdbcRepository.save(build2);

        //when
        InquiryFindReqDto result = inquiryJdbcRepository.findById(1L);

        //then
        assertThat(result.getTitle()).isEqualTo("제목1");

    }

    @Test
    @DisplayName("전체 조회 테스트")
    void findAll(){
        //given
        User save = userRepository.save(createUser("bbb@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        InquirySaveReqDto build3 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목3").content("내용3").user_id(save.getId()).build();
        inquiryJdbcRepository.save(build1);
        inquiryJdbcRepository.save(build2);
        inquiryJdbcRepository.save(build3);

        //when
        List<InquiryFindReqDto> result = inquiryJdbcRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void update(){
        //given
        User save = userRepository.save(createUser("ccc@naver.com"));
        InquirySaveReqDto build = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("안녕").content("하세요").user_id(save.getId()).build();
        inquiryJdbcRepository.save(build);

        //when
        InquiryUpdateReqDto update = InquiryUpdateReqDto.builder().inquiry_id(save.getId()).title("수정된 제목").content("수정된 내용").build();
        inquiryJdbcRepository.update(update);

        //then
        assertThat(update.getTitle()).isEqualTo("수정된 제목");
    }

    @Test
    @DisplayName("키워드(제목)으로 검색 테스트")
    void findByTitle(){
        //given
        User save = userRepository.save(createUser("ddd@naver.com"));
        InquirySaveReqDto build1 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        InquirySaveReqDto build3 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("하이").content("내용3").user_id(save.getId()).build();
        InquirySaveReqDto build4 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("1제목2").content("내용4").user_id(save.getId()).build();

        inquiryJdbcRepository.save(build1);
        inquiryJdbcRepository.save(build2);
        inquiryJdbcRepository.save(build3);
        inquiryJdbcRepository.save(build4);

        //when
        List<InquiryFindReqDto> actual = inquiryJdbcRepository.findByTitle("제목");

        //then
        assertThat(actual.get(0).getTitle()).isEqualTo("제목1");
        for(int i=0;i< actual.size();i++){
            System.out.println(actual.get(i).getTitle() + " / " + actual.get(i).getContent());
        }
    }

    @Test
    @DisplayName("게시글 삭제")
    void delete(){
        //given
        User save = userRepository.save(createUser("eee@naver.com"));
        InquirySaveReqDto build = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목1").content("내용1").user_id(save.getId()).build();
        InquirySaveReqDto build2 = InquirySaveReqDto.builder().inquiryState(InquiryState.ETC).title("제목2").content("내용2").user_id(save.getId()).build();
        inquiryJdbcRepository.save(build);
        inquiryJdbcRepository.save(build2);

        //when
        inquiryJdbcRepository.delete(1L);

        //then
        List<InquiryFindReqDto> result = inquiryJdbcRepository.findAll();
        assertThat(result.size()).isEqualTo(1);
    }
}