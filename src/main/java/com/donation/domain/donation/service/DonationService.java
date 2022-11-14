package com.donation.domain.donation.service;

import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.entity.Donation;
import com.donation.domain.donation.repository.DonationRepository;
import com.donation.domain.post.entity.Post;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.post.service.PostService;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.presentation.auth.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void createDonate(LoginMember loginMember,Long postId,DonationSaveReqDto donationSaveReqDto) {
        User user = userRepository.getById(loginMember.getId());
        Post post = postRepository.getById(postId);
        donationRepository.save(Donation.of(user, post, donationSaveReqDto.getAmount()));
        postService.increase(post.getId(), donationSaveReqDto.getFloatAmount());
    }

    public List<DonationFindRespDto> findMyDonation(LoginMember loginMember) {
        List<Donation> donations = donationRepository.findAllByUserId(loginMember.getId());
        Map<Long, Float> grossAmount = getGrossAmount(donations);
        return donations.stream()
                .map((Donation donation) -> DonationFindRespDto.of(donation,grossAmount.get(donation.getPost().getId())))
                .collect(Collectors.toList());
    }

    public List<DonationFindByFilterRespDto> findAllDonationByFilter(LoginMember loginMember,DonationFilterReqDto donationFilterReqDto){
        User user = userRepository.getById(loginMember.getId());
        user.validateAdmin();

        return donationRepository.findAllByFilter(donationFilterReqDto);
    }

    public Map<Long,Float> getGrossAmount(List<Donation> donations){
        Map<Long, Float> collect = donations.stream().collect(Collectors.toMap(
                i1 -> i1.getPost().getId(),
                i1 -> Donation.toFloat(i1.getAmount()),
                (oldValue, newValue) -> {
                    return oldValue + newValue;
                }
        ));
        return collect;
    }

}
