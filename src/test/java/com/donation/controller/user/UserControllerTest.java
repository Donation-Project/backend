package com.donation.controller.user;

import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.user.UserRepository;
import com.donation.service.s3.AwsS3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConstConfig config;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager em;
    @Autowired
    private AwsS3Service awsS3Service;

    @AfterEach
    void clear() {
        userRepository.deleteAll();
    }

    User getUser() {
        String username = "username@naver.com";
        String name = "정우진";
        String password = "1234";
        Role role = Role.USER;

        return User.builder()
                .username(username)
                .name(name)
                .password(password)
                .profileImage(config.getBasicImageProfile())
                .role(role)
                .build();
    }

    @Test
    @DisplayName("회원(컨트롤러) : 회원 가입")
    void join() throws Exception {
        //given
        UserJoinReqDto request = UserJoinReqDto.builder()
                .email("user@naver.com")
                .name("name")
                .password("password")
                .build();
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/join")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원(RestDocs) : 로그인")
    void login() throws Exception {
        //given
        User user = userRepository.save(getUser());
        UserLoginReqDto request = UserLoginReqDto.builder()
                .email(user.getUsername())
                .password(user.getPassword())
                .build();

        // expected
        mockMvc.perform(post("/api/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").value(user.getId()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원(컨트롤러) : 중복된 이메일 회원 가입")
    void join_exception() throws Exception {
        //given
        UserJoinReqDto request = UserJoinReqDto.builder()
                .email("user@naver.com")
                .name("name")
                .password("password")
                .build();

        userRepository.save(request.toUser(null));
        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/api/join")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.error.errorMessage").value(String.format("이미 존재하는 이메일 입니다.(%s)",request.getEmail())))
                .andDo(print());
    }

    @Test
    @DisplayName("회원(컨트롤러) : 리스트 조회")
    void list() throws Exception {
        //given
        List<User> users = IntStream.range(1, 31)
                .mapToObj(i -> User.builder()
                        .username("username@naver.com" + i)
                        .name("name" + i)
                        .password("password" + i)
                        .build()
                )
                .collect(Collectors.toList());
        userRepository.saveAll(users);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.data.content[0].username").value(users.get(0).getUsername()))
                .andExpect(jsonPath("$.data.content[0].name").value(users.get(0).getName()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원(컨트롤러) : 단건 조회")
    void get() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{postId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.profileImage").value(user.getProfileImage()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("회원(컨트롤러) : 단건 조회 없는 회원 조회")
    void get_exception() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{postId}", user.getId() + 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("NOT_FOUND"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원(컨트롤러) : 회원 삭제")
    void delete_exception() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        // expected
        mockMvc.perform(delete("/api/user/{userId}", user.getId() + 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.error.errorMessage").value("회원을 찾을수 없습니다."))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("회원(정보수정) : 프로필 변경")
    void update_profile() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        MultipartFile profile = new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "test1".getBytes());

        // expected
        mockMvc.perform(multipart("/api/user/{postId}/profile", user.getId())
                        .file("profile", profile.getBytes())
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("PUT");
                            return requestPostProcessor;
                        })
                        .contentType(MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

        em.flush();
        em.clear();

        //S3 파일 삭제
        User find = userRepository.findById(user.getId()).get();
        awsS3Service.delete(find.getProfileImage());
    }
}