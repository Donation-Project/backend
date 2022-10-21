package com.donation.repository.donation;

import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
public interface DonationRepositoryCustom {

    List<DonationFindRespDto> findAllByUserId(Long id);

    Slice<DonationFindByFilterRespDto> findAllByFilter(Pageable pageable, DonationFilterReqDto donationFilterReqDto);

    }