package com.donation.web.controller.auth;

import com.donation.auth.LoginInfo;
import com.donation.auth.LoginMember;
import com.donation.common.CommonResponse;
import com.donation.common.request.auth.TokenRenewalRequest;
import com.donation.common.response.auth.AccessTokenResponse;
import com.donation.service.auth.application.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/token/access")
    public ResponseEntity<?> generateAccessToken(
            @Valid @RequestBody final TokenRenewalRequest tokenRenewalRequest) {
        AccessTokenResponse response = authService.renewalToken(tokenRenewalRequest);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/validate/token")
    public ResponseEntity<Void> validateToken(@LoginInfo final LoginMember loginMember) {
        System.out.println("loginMember = " + loginMember.getId());
        return ResponseEntity.ok().build();
    }
}
