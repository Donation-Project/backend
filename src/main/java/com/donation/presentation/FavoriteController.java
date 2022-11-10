package com.donation.presentation;

import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.global.exception.DonationNotFoundException;
import com.donation.domain.favorite.application.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favorite")
@Slf4j
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{id}")
    public ResponseEntity<?> saveAndCancel(
            @LoginInfo LoginMember loginMember,
            @PathVariable(name = "id") Long postId,
            @RequestParam String type){

        switch (type){
            case "SAVE": favoriteService.save(loginMember, postId); break;
            case "CANCEL" : favoriteService.cancel(loginMember, postId); break;
            default: throw new DonationNotFoundException("잘못된 정보입니다.");
        }
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserList(@PathVariable(name = "id") Long postId){
        List<UserRespDto> userRespDto = favoriteService.findAll(postId);
        return ResponseEntity.ok(CommonResponse.success(userRespDto));
    }
}
