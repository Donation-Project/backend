package com.donation.web.controller.donation;

import com.donation.common.CommonResponse;
import com.donation.common.request.donation.DonationFilterReqDto;
import com.donation.common.request.donation.DonationSaveReqDto;
import com.donation.common.response.donation.DonationFindByFilterRespDto;
import com.donation.common.response.donation.DonationFindRespDto;
import com.donation.domain.enums.Category;
import com.donation.service.donation.DonationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
        donationService.save(donationSaveReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        List<DonationFindRespDto> donationFindRespDtos = donationService.findById(id);
        return ResponseEntity.ok(CommonResponse.success(donationFindRespDtos));
    }

    @GetMapping
    public ResponseEntity<?> getList(Pageable pageable,
                                     @RequestParam(required = false)String username,
                                     @RequestParam(required = false)Category category) {
        Slice<DonationFindByFilterRespDto> list = donationService.getList(pageable, new DonationFilterReqDto(username,category));
        return ResponseEntity.ok(CommonResponse.success(list));
    }


}
