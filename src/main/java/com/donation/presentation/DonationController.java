package com.donation.presentation;

import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.post.entity.Category;
import com.donation.domain.donation.service.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/donation")
public class DonationController {

    private final DonationService donationService;

    @PostMapping
    public ResponseEntity<?> donation(
            @RequestBody @Valid DonationSaveReqDto donationSaveReqDto
    ) {
        donationService.createDonate(donationSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<?> findMyDonation(@LoginInfo LoginMember loginMember) {
        List<DonationFindRespDto> donationFindRespDtos = donationService.findMyDonation(loginMember);
        return ResponseEntity.ok(CommonResponse.success(donationFindRespDtos));
    }
    @GetMapping
    public ResponseEntity<?> findAllDonationByFilter(@RequestBody DonationFilterReqDto donationFilterReqDto) {
        List<DonationFindByFilterRespDto> list = donationService.findAllDonationByFilter(donationFilterReqDto);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
}
