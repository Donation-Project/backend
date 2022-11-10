package com.donation.domain.donation.repository;

import com.donation.common.utils.RepositoryTest;
import com.donation.domain.donation.repository.DonationRepository;
import com.donation.domain.donation.entity.Donation;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.donation.common.DonationFixtures.*;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;
import static org.assertj.core.api.Assertions.assertThat;

public class DonationRepositoryTest extends RepositoryTest {
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("유저_아이디로_후원내역을_조회한다.")
    void 유저_아이디로_후원내역을_조회한다() {
        //given
        User sponsor = userRepository.save(createUser(후원자));
        User beneficiary = userRepository.save(createUser(후원_받는_사람));

        Post post = postRepository.save(createPost(beneficiary));

        Donation donation = donationRepository.save(createDonation(sponsor, post, 후원금액));


        //when
        List<Donation> allByUserId = donationRepository.findAllByUserId(sponsor.getId());

        //then
        Assertions.assertThat(allByUserId.get(0).getId()).isEqualTo(donation.getId());
    }


}
