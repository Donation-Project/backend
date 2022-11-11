package com.donation.domain.donation.service;

import com.donation.presentation.auth.LoginMember;
import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.entity.Donation;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.donation.repository.DonationRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import com.donation.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DonationService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final DonationRepository donationRepository;
    private final PostService postService;

    @Transactional
    public void createDonate(DonationSaveReqDto donationSaveReqDto) {
        User user = userRepository.getById(donationSaveReqDto.getUserId());
        Post post = postRepository.getById(donationSaveReqDto.getPostId());
        donationRepository.save(Donation.of(user, post, donationSaveReqDto.getAmount()));
        postService.increase(post.getId(), donationSaveReqDto.getFloatAmount());
    }

    public List<DonationFindRespDto> findById(LoginMember loginMember) {
        List<Donation> donations = donationRepository.findAllByUserId(loginMember.getId());
        Map<Long, Float> gross_amount = new HashMap<>();
        for (Donation donation : donations) {
            if(gross_amount.containsKey(donation.getPost().getId())){
                gross_amount.put(donation.getPost().getId(),Float.parseFloat(donation.getAmount())+gross_amount.get(donation.getPost().getId()));
            }
            else {
                gross_amount.put(donation.getPost().getId(),Float.parseFloat(donation.getAmount()));
            }
        }
        return donations.stream()
                .map((Donation donation) -> DonationFindRespDto.of(donation,gross_amount.get(donation.getPost().getId())))
                .collect(Collectors.toList());
    }

    public List<DonationFindByFilterRespDto> getList(DonationFilterReqDto donationFilterReqDto){
        return donationRepository.findAllByFilter(donationFilterReqDto);
    }

}
