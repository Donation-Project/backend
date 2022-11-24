package com.donation.presentation;

import com.donation.domain.user.dto.*;
import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.auth.dto.AccessAndRefreshTokenResponse;
import com.donation.domain.user.dto.UserEmailRespDto;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.infrastructure.util.PageCustom;
import com.donation.domain.auth.application.AuthService;
import com.donation.domain.user.application.UserService;
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

    @PostMapping("/join/exists")
    public ResponseEntity<?> validateUniqueEmail(@RequestBody @Valid UserEmailReqDto userEmailReqDto){
        UserEmailRespDto userEmailRespDto = userService.checkUniqueEmail(userEmailReqDto.getEmail());
        return ResponseEntity.ok(CommonResponse.success(userEmailRespDto));
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

    @PutMapping("/user/pw")
    public ResponseEntity<?> modifyPassword(
            @LoginInfo LoginMember loginMember,
            @RequestBody @Valid UserPasswordModifyReqDto userPasswordModifyReqDto){
        userService.passwordModify(loginMember, userPasswordModifyReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
