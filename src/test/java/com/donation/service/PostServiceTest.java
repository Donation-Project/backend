package com.donation.service;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.domain.embed.Write;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.UserRepository;
import com.donation.service.post.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.domain.enums.PostState.APPROVAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;


    @Autowired
    private UserRepository userRepository;

    static User getUser() {
        String username = "username@naver.com";
        String name = "홍길동";
        String password = "1234";
        Role role = Role.USER;

        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .role(role)
                .build();
    }

    @Test
    void createUser(){
        User user = getUser();
        userRepository.save(user);
    }

    @Test
    @DisplayName("포스트 : 생성")
    void save(){
        //given 데이터 주어짐
        Write write= Write.builder().title("제목").content("내용").build();

        PostSaveReqDto dto = getSaveReqDto();

        //when 상황이 주어짐
        PostRespDto savePost1 = postService.saveV1(dto,1L);

        //then 결과가 나와야함

        assertThat(savePost1.getContent()).isEqualTo(dto.getContent());

    }

    private static PostSaveReqDto getSaveReqDto() {
        PostSaveReqDto dto = new PostSaveReqDto();
        dto.setContent("content");
        dto.setTitle("title");
        dto.setAmount(12);
        dto.setCategory(ETC);
        dto.setState(APPROVAL);
        return dto;
    }

    @Test
    @DisplayName("포스트 : 생성시 중복처리")
    void overlap(){
        //given 데이터 주어짐
        Write write= Write.builder().title("제목").content("내용").build();
        User user=User.builder().build();
        PostSaveReqDto dto = getSaveReqDto();

        //when 상황이 주어짐

        //then 결과가 나와야함 한유저가 같이름의 제목 검사
        assertThatThrownBy(() ->postService.saveV1(dto,1L))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
