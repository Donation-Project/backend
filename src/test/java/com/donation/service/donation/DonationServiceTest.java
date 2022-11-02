package com.donation.service.donation;

import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.common.utils.ServiceTest;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.donation.common.DonationFixtures.기부_생성_DTO;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
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
        donationService.createDonate(기부_생성_DTO(user.getId(), post.getId(), amount));

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

        DonationSaveReqDto 존재하지않는_유저 = 기부_생성_DTO(null, post.getId(), amount);
        DonationSaveReqDto 존재하지않는_게시물 = 기부_생성_DTO(user.getId(), null, amount);

        //when & then
        assertAll(() -> {
            assertThatThrownBy(() ->  donationService.createDonate(존재하지않는_유저))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
            assertThatThrownBy(() ->  donationService.createDonate(존재하지않는_게시물))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
        });
    }


//    @Test
//    @DisplayName("후원(서비스) :  내후원 내역조회")
//    void get(){
//        //given
//        User user = userRepository.save(createUser("beneficiary@email.com"));
//        Post post = postRepository.save(createPost(user));
//        User sponsor = userRepository.save(createUser("sponsor@email.com"));
//        List<Donation> donations = IntStream.range(1, 31)
//                .mapToObj(i ->createDonation(sponsor,post,"10.1"+i)
//                ).collect(Collectors.toList());
//        donationRepository.saveAll(donations);
//        List<DonationFindRespDto> donationFindRespDtos = donationService.findById(sponsor.getId());
//        assertThat(donations.get(0).getPost().getWrite().getTitle()).isEqualTo(donationFindRespDtos.get(0).getTitle());
//        assertThat(donations.get(0).getAmount()).isEqualTo(donationFindRespDtos.get(0).getAmount());
//
//    }
//
//    @Test
//    @DisplayName("후원(서비스) :  아이디, 카테고리로 모든후원조회")
//    void getList(){
//        //given
//        User user = userRepository.save(createUser());
//        Post post = postRepository.save(createPost(user));
//        User sponsor = userRepository.save(createUser("sponsor@email.com"));
//        List<Donation> donations = IntStream.range(1, 31)
//                .mapToObj(i ->createDonation(sponsor,post,"10.1"+i)
//                ).collect(Collectors.toList());
//        donationRepository.saveAll(donations);
//        DonationFilterReqDto donationFilterReqDto = DonationFilterReqDto.builder().build();
//        Pageable pageable = PageRequest.of(0, 10);
//        Slice<DonationFindByFilterRespDto> donationList = donationService.getList(pageable, donationFilterReqDto);
//        assertThat(donationList.getSize()).isEqualTo(10);
//        assertThat(donationList.getNumberOfElements()).isEqualTo(10);
//        assertThat(donationList.getContent().get(0).getTitle()).isEqualTo(donations.get(0).getPost().getWrite().getTitle());
//        assertThat(donationList.getContent().get(0).getAmount()).isEqualTo(donations.get(0).getAmount());
//
//    }


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