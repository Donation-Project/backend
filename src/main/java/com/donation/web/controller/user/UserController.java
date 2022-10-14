package com.donation.web.controller.user;

import com.donation.common.CommonResponse;
import com.donation.common.response.user.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @description 회원 정보 CRUD 컨트롤러
 * @author  정우진
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid UserJoinReqDto userJoinReqDto) {
        userService.join(userJoinReqDto);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginReqDto userLoginReqDto){
        userService.login(userLoginReqDto);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getList(Pageable pageable){
        Slice<UserRespDto> list = userService.getList(pageable);
        return ResponseEntity.ok(CommonResponse.success(list));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        UserRespDto userRespDto = new UserRespDto(userService.get(id));
        return ResponseEntity.ok(CommonResponse.success(userRespDto));
    }

    @PutMapping("/user/{id}/profile")
    public ResponseEntity<?> editProfile(@PathVariable Long id,
                                    @RequestPart("profile") MultipartFile multipartFile){
        userService.editProfile(id, multipartFile);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.ok(CommonResponse.success());
    }
}
