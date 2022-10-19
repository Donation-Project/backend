package com.donation.service.donation;

import com.donation.common.request.donation.DonationSaveReqDto;

import com.donation.domain.embed.Write;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;

import com.donation.domain.enums.PostState;
import com.donation.domain.enums.Role;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.donation.domain.enums.Category.ETC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DonationServiceTest {
    @Autowired
    private DonationService donationService;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clear(){
        donationRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();

    }

    User getUser() {
        User user = User.builder()
                .username("username@naver.com")
                .name("장원진")
                .password("1234")
                .role(Role.USER)
                .build();
        return userRepository.save(user);
    }
    Post getPost() {
        Post post = Post.builder()
                .user(getUser())
                .write(new Write("title", "content"))
                .amount(10)
                .category(ETC)
                .state(PostState.WAITING)
                .build();
        return postRepository.save(post);
    }
    private User getSponsor() {
        User sponsor = User.builder()
                .username("username1@naver.com")
                .name("장원진1")
                .password("12341")
                .role(Role.USER)
                .build();
        return userRepository.save(sponsor);
    }


    @Test
    @DisplayName("후원(서비스) : 생성")
    void save(){
        //given
        Post post = getPost();
        User sponsor = getSponsor();

        DonationSaveReqDto request = new DonationSaveReqDto(sponsor.getId(),post.getId(),10.1f);

        //when
        Long donationId = donationService.save(request);

        //then
        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(donation.getPost().getId()).isEqualTo(post.getId());
        assertThat(donation.getUser().getId()).isEqualTo(sponsor.getId());
    }




}