package com.donation.service.user;

import com.donation.common.reponse.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.domain.entites.User;
import com.donation.exception.EmailDuplicateException;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${basicProfile}")
    private String value;

    /**
     *    public void join(UserJoinReqDto userJoinReqDto) {
     *         User user = userJoinReqDto.toUser();
     *         if (userRepository.findByUsername(userJoinReqDto.getEmail()).isPresent())
     *             throw new EmailDuplicateException();
     *
     *         userRepository.save(user);
     *     }
     */

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
    void get(){
        //given
        List<User> users = IntStream.range(1, 31)
                .mapToObj(i -> User.builder()
                        .username("username" + i)
                        .name("name" + i)
                        .password("password" + i)
                        .build()
                )
                .collect(Collectors.toList());


        userRepository.saveAll(users);

        //when

    }
}