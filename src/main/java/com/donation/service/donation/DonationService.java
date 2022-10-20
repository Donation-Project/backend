package com.donation.service.donation;

import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.common.response.donation.QDonationFindRespDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.service.user.UserService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.donation.domain.entites.QDonation.donation;
import static org.aspectj.runtime.internal.Conversions.floatValue;


@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {

    private final UserService userService;

    private final PostRepository postRepository;

    private final DonationRepository donationRepository;

    public void save(DonationSaveReqDto donationSaveReqDto) {
        Donation donation = Donation.builder()
                .user(userService.get(donationSaveReqDto.getUserId()))
                .post(postRepository.findById(donationSaveReqDto.getPostId())
                        .orElseThrow(IllegalArgumentException::new))
                .amount(10.1f)
                .build();
        donationRepository.save(donation);

    }

    public  List<DonationFindRespDto> findById(Long userId) {
        return donationRepository.findAllByUserId(userId);
    }
}
