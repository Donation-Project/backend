package com.donation.web.controller.favortie;

import com.donation.common.CommonResponse;
import com.donation.common.response.user.UserRespDto;
import com.donation.service.favorite.FavoriteService;
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

    @PostMapping
    public ResponseEntity<?> saveAndCancel(@RequestParam Long postId, @RequestParam Long userId){
        log.info("{} {}",postId, userId);
        favoriteService.saveAndCancel(postId, userId);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long postId){
        favoriteService.deletePostId(postId);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping
    public ResponseEntity<?> getUserList(@RequestParam Long postId){
        List<UserRespDto> userRespDto = favoriteService.findAll(postId);
        return ResponseEntity.ok(CommonResponse.success(userRespDto));
    }
}
