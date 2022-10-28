package com.donation.service.donation;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.donation.common.TestEntityDataFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @AfterEach
    void clear(){
        donationRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("후원(서비스) :  후원하기_예외발생")
    void save(){
        //given
        User user = userRepository.save(createUser("beneficiary@email.com"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor@email.com"));
        DonationSaveReqDto donationWithoutPost = new DonationSaveReqDto(sponsor.getId(),null,"10.1");
        DonationSaveReqDto donationWithoutUser = new DonationSaveReqDto(null,post.getId(),"10.1");
        DonationSaveReqDto donation = new DonationSaveReqDto(sponsor.getId(),post.getId(),"10.1");
        donationService.save(donation);
        //when
        assertThatThrownBy(() ->  donationService.save(donationWithoutPost))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);

        assertThatThrownBy(() ->  donationService.save(donationWithoutUser))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
    @Test
    @DisplayName("후원(서비스) :  내후원 내역조회")
    void get(){
        //given
        User user = userRepository.save(createUser("beneficiary@email.com"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor@email.com"));
        List<Donation> donations = IntStream.range(1, 31)
                .mapToObj(i ->createDonation(sponsor,post,"10.1"+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);
        List<DonationFindRespDto> donationFindRespDtos = donationService.findById(sponsor.getId());
        assertThat(donations.get(0).getPost().getWrite().getTitle()).isEqualTo(donationFindRespDtos.get(0).getTitle());
        assertThat(donations.get(0).getAmount()).isEqualTo(donationFindRespDtos.get(0).getAmount());

    }

    @Test
    @DisplayName("후원(서비스) :  아이디, 카테고리로 모든후원조회")
    void getList(){
        //given
        User user = userRepository.save(createUser("beneficiary@email.com"));
        Post post = postRepository.save(createPost(user));
        User sponsor = userRepository.save(createUser("sponsor@email.com"));
        List<Donation> donations = IntStream.range(1, 31)
                .mapToObj(i ->createDonation(sponsor,post,"10.1"+i)
                ).collect(Collectors.toList());
        donationRepository.saveAll(donations);
        DonationFilterReqDto donationFilterReqDto = DonationFilterReqDto.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Slice<DonationFindByFilterRespDto> donationList = donationService.getList(pageable, donationFilterReqDto);
        assertThat(donationList.getSize()).isEqualTo(10);
        assertThat(donationList.getNumberOfElements()).isEqualTo(10);
        assertThat(donationList.getContent().get(0).getTitle()).isEqualTo(donations.get(0).getPost().getWrite().getTitle());
        assertThat(donationList.getContent().get(0).getAmount()).isEqualTo(donations.get(0).getAmount());
        
    }




}