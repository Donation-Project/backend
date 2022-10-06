package com.donation.web.controller;

import com.donation.common.CommonResponse;
import com.donation.common.request.LoginReqDto;
import com.donation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/loginForm")
    public ResponseEntity<?> login(@RequestBody @Valid LoginReqDto loginReqDto) {
        userService.login(loginReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }


}
