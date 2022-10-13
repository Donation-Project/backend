package com.donation.service.user;

import com.donation.common.reponse.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.domain.entites.User;
import com.donation.exception.EmailDuplicateException;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clear(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원(서비스) : 회원 가입")
    void join(){
        //given
        UserJoinReqDto userDto = UserJoinReqDto.builder()
                .email("woojin@naver.com")
                .name("woojin")
                .password("password")
                .build();

        //when
        Long id = userService.join(userDto);

        //then
        UserRespDto userRespDto = userService.get(id);
        assertThat(userRespDto.getUsername()).isEqualTo(userDto.getEmail());
        assertThat(userRespDto.getName()).isEqualTo(userDto.getName());
    }

    @Test
    @DisplayName("회원(서비스) : 회원가입 중복된 이메일")
    void join_duplicate_email(){
        //given
        UserJoinReqDto userDto = UserJoinReqDto.builder()
                .email("woojin@naver.com")
                .name("woojin")
                .password("password")
                .build();

        //when
        Long id = userService.join(userDto);

        //then
        assertThatThrownBy(() -> userService.join(userDto))
                .isInstanceOf(EmailDuplicateException.class);
    }

    @Test
    @DisplayName("회원(서비스) : 전체 조회(페이징)")
    void getList(){
        //given
        List<User> users = IntStream.range(1, 31)
                .mapToObj(i -> User.builder()
                        .username("username" + i + "@naver.com")
                        .name("name" + i)
                        .password("password" + i)
                        .build()
                )
                .collect(Collectors.toList());
        userRepository.saveAll(users);

        Pageable pageable = PageRequest.of(0, 10);
        //when
        Slice<UserRespDto> userList = userService.getList(pageable);

        //then
        assertThat(userList.getSize()).isEqualTo(10);
        assertThat(userList.getNumberOfElements()).isEqualTo(10);
        assertThat(userList.getContent().get(0).getName()).isEqualTo(users.get(0).getName());
    }

    @Test
    @DisplayName("회원(서비스) : 단건 조회")
    void get() {
        //given
        UserJoinReqDto userDto = UserJoinReqDto.builder()
                .email("woojin@naver.com")
                .name("woojin")
                .password("password")
                .build();
        Long id = userService.join(userDto);
        //when
        UserRespDto dto = userService.get(id);

        //then
        assertThat(dto.getUsername()).isEqualTo(userDto.getEmail());
        assertThat(dto.getName()).isEqualTo(userDto.getName());
    }

    @Test
    @DisplayName("회원(서비스) : 단건 조회 예외발생")
    void get_exception() {
        //given
        Long id = 100L;
        //then
        assertThatThrownBy(() -> userService.get(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("회원(서비스) : 회원 삭제")
    void delete(){
        //given
        UserJoinReqDto userDto = UserJoinReqDto.builder()
                .email("woojin@naver.com")
                .name("woojin")
                .password("password")
                .build();
        Long id = userService.join(userDto);

        //when
        userService.delete(id);

        //then
        assertThatThrownBy(() -> userService.get(id))
                .isInstanceOf(IllegalArgumentException.class);
    }
}