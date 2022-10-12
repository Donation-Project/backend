package com.donation.web.controller;

import com.donation.common.CommonResponse;
import com.donation.common.reponse.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserJoinReqDto userJoinReqDto) {
        userService.join(userJoinReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto){
        userService.login(userLoginReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/user")
    public ResponseEntity<?> list(){
        List<UserRespDto> users = userService.getList();
        return ResponseEntity.ok(CommonResponse.success(users));
    }
}
