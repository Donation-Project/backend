package com.donation.repository.user;

import com.donation.common.response.user.UserRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserRepositoryCustom {
    public Slice<UserRespDto> findPageableAll(Pageable pageable);
}
