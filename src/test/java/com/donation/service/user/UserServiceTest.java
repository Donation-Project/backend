package com.donation.service.user;

import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.response.user.UserRespDto;
import com.donation.domain.entites.User;
import com.donation.exception.DonationException;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.testutil.TestDtoDataFactory.createUserJoinReqDto;
import static com.donation.testutil.TestEntityDataFactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clear(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원(서비스) : 회원 가입")
    void join(){
        //given
        UserJoinReqDto userDto = createUserJoinReqDto();

        //when
        Long id = userService.join(userDto);

        //then
        UserRespDto userRespDto = new UserRespDto(userService.findById(id));
        assertThat(userRespDto.getUsername()).isEqualTo(userDto.getEmail());
        assertThat(userRespDto.getName()).isEqualTo(userDto.getName());
    }

    @Test
    @DisplayName("회원(서비스) : 회원가입 중복된 이메일")
    void join_duplicate_email(){
        //given
        UserJoinReqDto userDto = createUserJoinReqDto();

        //when
        Long id = userService.join(userDto);

        //then
        assertThatThrownBy(() -> userService.join(userDto))
                .isInstanceOf(DonationException.class);
    }

    @Test
    @DisplayName("회원(서비스) : 전체 조회(페이징)")
    void getList(){
        //given
        List<User> users = IntStream.range(1, 31)
                .mapToObj(i -> createUser("username" + i))
                .collect(Collectors.toList());
        userRepository.saveAll(users);

        //when
        Slice<UserRespDto> userList = userService.getList(PageRequest.of(0, 10));

        //then
        assertThat(userList.getSize()).isEqualTo(10);
        assertThat(userList.getNumberOfElements()).isEqualTo(10);
        assertThat(userList.getContent().get(0).getName()).isEqualTo(users.get(0).getName());
    }

    @Test
    @DisplayName("회원(서비스) : 단건 조회")
    void get() {
        //given
        User user = userRepository.save(createUser());

        //when
        UserRespDto dto = new UserRespDto(userService.findById(user.getId()));

        //then
        assertThat(dto.getUsername()).isEqualTo(user.getUsername());
        assertThat(dto.getName()).isEqualTo(user.getName());
    }

    @Test
    @DisplayName("회원(서비스) : 단건 조회 예외발생")
    void get_exception() {
        //given
        Long id = 100L;
        //then
        assertThatThrownBy(() -> userService.findById(id))
                .isInstanceOf(DonationException.class);
    }

    @Test
    @DisplayName("회원(서비스) : 회원 삭제")
    void delete(){
        //given
        User user = userRepository.save(createUser());

        //when
        userService.delete(user.getId());

        //then
        assertThatThrownBy(() -> userService.findById(user.getId()))
                .isInstanceOf(DonationException.class);
    }
}