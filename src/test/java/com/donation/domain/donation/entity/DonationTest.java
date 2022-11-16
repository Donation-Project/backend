package com.donation.domain.donation.entity;


import com.donation.global.exception.DonationInvalidateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.donation.common.DonationFixtures.*;
import static com.donation.common.PostFixtures.createPost;
import static com.donation.common.UserFixtures.createUser;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class DonationTest {

    @Test
    @DisplayName("후원을 한다.")
    void 후원을_한다(){
        //given & when & then
        assertDoesNotThrow(() -> createDonation(createUser(),createPost(),후원금액));
    }
    @Test
    @DisplayName("후원원시 최소금액나 최대금액을 벗어나면 예외를 던진다.")
    void 후원시_최소금액나_최대금액을_벗어나면_예외를_던진다(){
        //given & when & then
        assertAll(() ->{
            assertThatThrownBy(() -> createDonation(createUser(),createPost(),최소금액을_벗어남))
                    .isInstanceOf(DonationInvalidateException.class);
            assertThatThrownBy(() -> createDonation(createUser(),createPost(),최대금액을_벗어남))
                    .isInstanceOf(DonationInvalidateException.class);
        });
    }
}
