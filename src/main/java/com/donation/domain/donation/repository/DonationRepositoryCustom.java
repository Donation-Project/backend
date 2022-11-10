package com.donation.domain.donation.repository;

import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;

import java.util.List;
public interface DonationRepositoryCustom {

    List<DonationFindByFilterRespDto> findAllByFilter(DonationFilterReqDto donationFilterReqDto);
    }