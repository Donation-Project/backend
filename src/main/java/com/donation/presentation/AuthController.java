package com.donation.presentation;

import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.auth.dto.TokenRenewalRequest;
import com.donation.domain.auth.dto.AccessTokenResponse;
import com.donation.domain.auth.application.AuthService;
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
