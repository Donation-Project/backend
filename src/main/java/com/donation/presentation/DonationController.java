package com.donation.presentation;

import com.donation.domain.donation.dto.DonationFilterReqDto;
import com.donation.domain.donation.dto.DonationFindByFilterRespDto;
import com.donation.domain.donation.dto.DonationFindRespDto;
import com.donation.domain.donation.dto.DonationSaveReqDto;
import com.donation.domain.donation.application.DonationService;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
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
@RequestMapping("/api")
public class DonationController {

    private final DonationService donationService;

    @PostMapping("/post/{id}/donation")
    public ResponseEntity<?> donation(
            @LoginInfo LoginMember loginMember,
            @PathVariable(name = "id") Long postId,
            @RequestBody @Valid DonationSaveReqDto donationSaveReqDto
    ) {
        donationService.createDonate(loginMember,postId,donationSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/donation/me")
    public ResponseEntity<?> findMyDonation(@LoginInfo LoginMember loginMember) {
        List<DonationFindRespDto> donationFindRespDtos = donationService.findMyDonation(loginMember);
        return ResponseEntity.ok(CommonResponse.success(donationFindRespDtos));
    }

    @GetMapping("/donation")
    public ResponseEntity<?> findAllDonationByFilter(@LoginInfo LoginMember loginMember,
            @RequestBody DonationFilterReqDto donationFilterReqDto) {
        List<DonationFindByFilterRespDto> list = donationService.findAllDonationByFilter(loginMember,donationFilterReqDto);
        return ResponseEntity.ok(CommonResponse.success(list));
    }


}
