package com.donation.service.donation;

import com.donation.auth.LoginMember;
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
import com.donation.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        return donationRepository.findAllByUserId(loginMember.getId()).stream()
                .map(DonationFindRespDto::of)
                .collect(Collectors.toList());
    }

    public List<DonationFindByFilterRespDto> getList(DonationFilterReqDto donationFilterReqDto){
        return donationRepository.findAllByFilter(donationFilterReqDto);
    }

}
