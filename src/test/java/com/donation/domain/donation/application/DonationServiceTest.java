package com.donation.domain.donation.application;

import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.donation.entity.Donation;
import com.donation.domain.donation.service.DonationService;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.global.exception.DonationNotFoundException;
import com.donation.domain.donation.repository.DonationRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.donation.common.AuthFixtures.회원검증;
import static com.donation.common.DonationFixtures.*;
import static com.donation.common.PostFixtures.*;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


class DonationServiceTest extends ServiceTest {
    @Autowired
    private DonationService donationService;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("후원 하기 성공")
    void 후원_하기_성공(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        String amount = "1";

        //given
        donationService.createDonate(기부_생성_DTO( user.getId(), post.getId(), amount));

        //then
        Assertions.assertThat(donationRepository.count()).isEqualTo(1L);
    }

    @Test
    @DisplayName("존재하지 않는 유저이거나 존재하지않는 게시물의 경우 예외를 던진다.")
    void 존재하지_않는_유저이거나_존재하지않는_게시물의_경우_예외를_던진다(){
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        String amount = "1";

        DonationSaveReqDto 존재하지않는_유저 = 기부_생성_DTO(2L, post.getId(), amount);
        DonationSaveReqDto 존재하지않는_게시물 = 기부_생성_DTO(user.getId(), null, amount);

        //when & then
        assertAll(() -> {
            assertThatThrownBy(() ->  donationService.createDonate(존재하지않는_유저))
                .isInstanceOf(DonationNotFoundException.class);
            assertThatThrownBy(() ->  donationService.createDonate(존재하지않는_게시물))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
        });
    }

    @Test
    @DisplayName("유저_아이디로_후원내역을_조회한다.")
    void 유저_아이디로_후원내역을_조회한다() {
        //given
        User sponsor = userRepository.save(createUser(후원자));
        User beneficiary = userRepository.save(createUser(후원_받는_사람));
        Post post = postRepository.save(createPost(beneficiary));
        Donation donation = donationRepository.save(createDonation(sponsor, post, 후원금액));

        //when
        List<DonationFindRespDto> dtos = donationService.findMyDonation(회원검증(sponsor.getId()));

        //then
        assertAll(() ->{
            assertThat(dtos.get(0).getUserId()).isEqualTo(sponsor.getId());
            assertThat(dtos.get(0).getPostId()).isEqualTo(post.getId());
            assertThat(dtos.get(0).getAmount()).isEqualTo(후원금액);
        });
    }

    @Test
    @DisplayName("모든_후원내역을_조회한다.")
    void 모든_후원내역을_조회한다() {
        //given
        User sponsor = userRepository.save(createUser(후원자));
        User beneficiary = userRepository.save(createUser(후원_받는_사람));
        Post post = postRepository.save(createPost(beneficiary));
        List<Donation> donations = donationRepository.saveAll(createDonationList(1, 5, sponsor, post));

        //when
        List<DonationFindByFilterRespDto> list = donationService.findAllDonationByFilter(기부_전체검색_DTO());

        //then
        assertAll(() ->{
            Assertions.assertThat(list.get(0).getDonateId()).isEqualTo(donations.get(0).getId());
            Assertions.assertThat(list.get(0).getPostId()).isEqualTo(donations.get(0).getPost().getId());
            Assertions.assertThat(list.get(0).getUserId()).isEqualTo(donations.get(0).getUser().getId());
        });

    }



    @Test
    void 동시에_100명이_기부를한다() throws InterruptedException {
        //given
        User user = userRepository.save(createUser());
        Post post = postRepository.save(createPost(user));
        String amount = "1";

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    donationService.createDonate(new DonationSaveReqDto(user.getId(),post.getId(), amount));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        Post actual = postRepository.findById(post.getId()).get();
        Assertions.assertThat(actual.getCurrentAmount()).isEqualTo(100);
    }
}