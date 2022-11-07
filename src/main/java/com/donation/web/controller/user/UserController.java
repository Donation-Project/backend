package com.donation.web.controller.user;

import com.donation.auth.LoginInfo;
import com.donation.auth.LoginMember;
import com.donation.common.CommonResponse;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserProfileUpdateReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.common.response.auth.AccessAndRefreshTokenResponse;
import com.donation.common.response.user.UserRespDto;
import com.donation.repository.utils.PageCustom;
import com.donation.service.auth.application.AuthService;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid UserSaveReqDto userSaveReqDto) {
        authService.save(userSaveReqDto);
        return ResponseEntity.created(URI.create("/api/user/me")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto){
        AccessAndRefreshTokenResponse token = authService.login(userLoginReqDto);
        return ResponseEntity.ok(CommonResponse.success(token));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getList(Pageable pageable){
        PageCustom<UserRespDto> users = userService.getList(pageable);
        return ResponseEntity.ok(CommonResponse.success(users));
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> get(@LoginInfo LoginMember loginMember){
        UserRespDto userRespDto = userService.findById(loginMember);
        return ResponseEntity.ok(CommonResponse.success(userRespDto));
    }

    @PutMapping("/user/profile")
    public ResponseEntity<?> editProfile(
            @LoginInfo LoginMember loginMember,
            @Valid @RequestBody UserProfileUpdateReqDto profileUpdateReqDto){
        userService.updateProfile(loginMember, profileUpdateReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
