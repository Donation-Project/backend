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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.domain.enums.Category.ETC;
import static com.donation.testutil.TestEntityDataFactory.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DonationControllerTest {
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
    @DisplayName("후원(컨트롤러) : 후원 하기")
    void save() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        DonationSaveReqDto data = new DonationSaveReqDto(sponsor.getId(), post.getId(), "10.1");
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
                .andDo(print());
    }

    @Test
    @DisplayName("후원(컨트롤러) : 후원 하기_예외발생")
    void saveError() throws Exception {
        //given
        User sponsor = userRepository.save(createUser("sponsor"));
        DonationSaveReqDto data = new DonationSaveReqDto(sponsor.getId(), null, "10.1");
        String request = objectMapper.writeValueAsString(data);

        //expected
        mockMvc.perform(post("/api/donation")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value("false"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error.errorCode").value("BAD_REQUEST"))
                .andDo(print());
    }

    @Test
    @DisplayName("후원(컨트롤러) : 내후원 조회")
    void findByUserId() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        List<Donation> donations = IntStream.range(1, 31)
                .mapToObj(i -> createDonation(sponsor,post,"10.1"+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);


        //expected
        mockMvc.perform(get("/api/donation/"+sponsor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data[0].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data[0].amount").value("10.11"))
                .andExpect(jsonPath("$.data[9].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data[9].amount").value("10.110"))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("후원(컨트롤러) : 전체 조회")
    void findAllByFilter() throws Exception {
        //given
        User user = userRepository.save(createUser("beneficiary"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor"));
        List<Donation> donations = IntStream.range(1, 31)
                .mapToObj(i -> createDonation(sponsor,post,"10.1"+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);

        DonationFilterReqDto dto=new DonationFilterReqDto(null, ETC);

        String request = objectMapper.writeValueAsString(dto);


        //expected
        mockMvc.perform(get("/api/donation")
                        .contentType(APPLICATION_JSON)
                        .content(request))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.data.content[0].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[0].amount").value("10.11"))
                .andExpect(jsonPath("$.data.content[3].title").value(post.getWrite().getTitle()))
                .andExpect(jsonPath("$.data.content[3].amount").value("10.14"))
                .andExpect(jsonPath("$.error").isEmpty())
                .andDo(print());

    }




}