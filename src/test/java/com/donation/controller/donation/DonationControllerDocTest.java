package com.donation.controller.donation;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.testutil.TestEntityDataFactory.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.springdocs.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class DonationControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void clear() {
        donationRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    @DisplayName("후원(RestDocs) : 후원 하기")
    void save() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        DonationSaveReqDto data = new DonationSaveReqDto(sponsor.getId(), post.getId(), 10.1f);
        String request = objectMapper.writeValueAsString(data);

        //expected
        mockMvc.perform(post("/api/donation")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("donation-save",
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
    @DisplayName("후원(RestDocs) : 내후원 조회")
    void findByUserId() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        List<Donation> donations = IntStream.range(1, 31)
                .mapToObj(i -> createDonation(sponsor,post,10.1f+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);


        //expected
        mockMvc.perform(get("/api/donation/{id}",sponsor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data[0].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data[0].amount").value(11.1f))
                .andExpect(jsonPath("$.data[9].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data[9].amount").value(20.1f))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("donation-getUserList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("유저 ID")
                        )
                ));

    }

    @Test
    @DisplayName("후원(RestDocs) : 전체 조회")
    void findAllByFilter() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        List<Donation> donations = IntStream.range(1, 21)
                .mapToObj(i -> createDonation(sponsor,post,10.1f+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);
        DonationFilterReqDto request = new DonationFilterReqDto(null, ETC);

        //expected
        mockMvc.perform(get("/api/donation")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content[0].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[0].amount").value(11.1f))
                .andExpect(jsonPath("$.data.content[3].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[3].amount").value(14.1f))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(document("donation-getList",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                ));

    }




}