package com.donation.domain.inquiry.application;

import com.donation.domain.inquiry.dto.InquiryFindReqDto;
import com.donation.domain.inquiry.dto.InquirySaveReqDto;
import com.donation.domain.inquiry.dto.InquiryUpdateReqDto;
import com.donation.domain.inquiry.repository.InquiryJdbcRepository;

import java.util.List;

public class InquiryService {
    private final InquiryJdbcRepository inquiryJdbcRepository;

    public InquiryService(InquiryJdbcRepository inquiryJdbcRepository) {
        this.inquiryJdbcRepository = inquiryJdbcRepository;
    }

    /**
     * 문의글 작성
     * @param inquirySaveReqDto
     */
    public void createInquiry(InquirySaveReqDto inquirySaveReqDto) {
        inquiryJdbcRepository.save(inquirySaveReqDto);
    }

    /**
     * 문의글 전체 조회
     * @return
     */
    public List<InquiryFindReqDto> viewAll() {
        return inquiryJdbcRepository.findAll();
    }

    /**
     * 문의글 수정
     * @param inquiryUpdateReqDto
     */
    public void updateInquiry(InquiryUpdateReqDto inquiryUpdateReqDto) {
        inquiryJdbcRepository.update(inquiryUpdateReqDto);
    }

    /**
     * 문의글 제목으로 검색
     * @param title
     * @return
     */
    public List<InquiryFindReqDto> findByTitle(String title) {
        return inquiryJdbcRepository.findByTitle(title);
    }

    /**
     * 문의글 삭제
     * @param inquiry_id
     */
    public void delete(Long inquiry_id) {
        inquiryJdbcRepository.delete(inquiry_id);
    }
}
