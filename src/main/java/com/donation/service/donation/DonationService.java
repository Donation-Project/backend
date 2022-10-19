package com.donation.service.donation;

import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.domain.entites.Donation;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DonationService {



    private final UserService userService;

    private final PostRepository postRepository;

    private final DonationRepository donationRepository;


    public Long save(DonationSaveReqDto donationSaveReqDto) {
        Donation donation = Donation.builder()
                .user(userService.get(donationSaveReqDto.getUserId()))
                .post(postRepository.findById(donationSaveReqDto.getPostId())
                        .orElseThrow(IllegalArgumentException::new))
                .build();
        return donationRepository.save(donation).getId();
    }
}
