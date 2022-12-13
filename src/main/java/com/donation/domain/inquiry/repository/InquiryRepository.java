package com.donation.domain.inquiry.repository;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.dto.InquiryUpdateReqDto;
import com.donation.domain.inquiry.entity.Inquiry;
import com.donation.domain.user.entity.User;
import com.donation.infrastructure.embed.Write;

import java.lang.reflect.Member;
import java.util.List;

public interface InquiryRepository {

    void save(InquirySaveReqDto inquirySaveReqDto);
    List<InquiryFindReqDto> findAll();
    void update(InquiryUpdateReqDto inquiryUpdateReqDto);
    List<InquiryFindReqDto> findByTitle(String title);
    void delete(Long inquiry_id);
    void clear();
}
