package com.donation.controller.user;


import com.donation.common.request.user.UserJoinReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.user.UserRepository;
import com.donation.service.s3.AwsS3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.springdocs.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConstConfig config;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AwsS3Service s3Service;


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
    @DisplayName("회원(RestDocs) : 회원 가입")
    void join() throws Exception {
        //given
        UserJoinReqDto request = UserJoinReqDto.builder()
                .email("user@naver.com")
                .name("name")
                .password("password")
                .build();

        // expected
        mockMvc.perform(post("/api/join")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("user-join",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data").description("데이터"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )
                ));
    }

    @Test
    @DisplayName("회원(RestDocs) : 단건 조회")
    void get() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        // expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/user/{id}", user.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.id").value(user.getId()))
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.name").value(user.getName()))
                .andExpect(jsonPath("$.data.profileImage").value(user.getProfileImage()))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("user-inquiry",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("유저 ID")
                        ),
                        responseFields(
                                fieldWithPath("success").description("성공 여부"),
                                fieldWithPath("data.id").description("유저 ID"),
                                fieldWithPath("data.username").description("이메일"),
                                fieldWithPath("data.name").description("이름"),
                                fieldWithPath("data.profileImage").description("회원 프로필 이미지"),
                                fieldWithPath("error").description("에러 발생시 오류 반환")
                        )

                ));
    }

    @Test
    @DisplayName("회원(RestDocs) : 회원 리스트 조회")
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
                .andDo(document("user-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }

    @Test
    @DisplayName("회원(컨트롤러) : 회원 삭제")
    void delete() throws Exception {
        //given
        User user = getUser();
        userRepository.save(user);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("user-delete",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));
    }
}
