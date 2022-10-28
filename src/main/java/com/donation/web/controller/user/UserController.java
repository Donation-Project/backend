package com.donation.web.controller.user;

import com.donation.common.CommonResponse;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserProfileUpdateReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.common.response.user.UserRespDto;
import com.donation.domain.entites.User;
import com.donation.repository.utils.PageCustom;
import com.donation.service.user.AuthService;
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
        Long id = authService.save(userSaveReqDto);
        return ResponseEntity.created(URI.create("/api/user/" + id)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto){
        User user = authService.login(userLoginReqDto);
        return ResponseEntity.ok(CommonResponse.success(user.getId()));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getList(Pageable pageable){
        PageCustom<UserRespDto> users = userService.getList(pageable);
        return ResponseEntity.ok(CommonResponse.success(users));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        UserRespDto userRespDto = userService.findById(id);
        return ResponseEntity.ok(CommonResponse.success(userRespDto));
    }

    @PutMapping("/user/{id}/profile")
    public ResponseEntity<?> editProfile(
            @PathVariable Long id,
            @Valid @RequestBody UserProfileUpdateReqDto profileUpdateReqDto){
        userService.updateProfile(id, profileUpdateReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
