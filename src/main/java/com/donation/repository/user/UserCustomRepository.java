package com.donation.repository.user;

import com.donation.common.reponse.user.UserRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserCustomRepository {
    public Slice<UserRespDto> findPageableAll(Pageable pageable);
}
