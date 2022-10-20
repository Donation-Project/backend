package com.donation.repository.donation;

import com.donation.common.response.donation.DonationFindRespDto;
import java.util.List;
public interface DonationRepositoryCustom {

    List<DonationFindRespDto> findAllByUserId(Long id);
}