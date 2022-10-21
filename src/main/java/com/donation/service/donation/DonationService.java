package com.donation.service.donation;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.domain.entites.Donation;
import com.donation.repository.donation.DonationRepository;
import com.donation.repository.post.PostRepository;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Slice<DonationFindByFilterRespDto> getList(Pageable pageable, DonationFilterReqDto donationFilterReqDto) {
        if (donationFilterReqDto == null)
            donationFilterReqDto = DonationFilterReqDto.builder().build();
        return donationRepository.findAllByFilter(pageable, donationFilterReqDto);

    }

}
