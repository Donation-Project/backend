package com.donation.common;

import com.donation.domain.auth.entity.AuthCode;
import com.donation.presentation.auth.LoginMember;
import com.donation.domain.auth.dto.TokenRenewalRequest;
import com.donation.domain.auth.dto.AccessAndRefreshTokenResponse;
import com.donation.domain.auth.dto.AccessTokenResponse;
import com.donation.domain.auth.entity.AuthToken;

public class AuthFixtures {


    public static final String 엑세스_토큰 = "aaaaaaaaaa.bbbbbbbbbb.ccccccccc";
    public static final String 리프레시_토큰 = "ccccccccc.bbbbbbbbbb.aaaaaaaaaa";
    public static final String 리뉴얼_엑세스_토큰 = "bbbbbbbbbb.ccccccccc.aaaaaaaaaa";
    public static final String 토큰_정보 = "Bearer " + 엑세스_토큰;
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /*
    * AuthCode
    * */
    public static final String 인증코드 = "AuthorizationCode";
    public static final String 시리얼넘버 = "SerialNumber";

    public static AuthCode 인증코드(){
        return AuthCode.of(인증코드, 시리얼넘버);
    }

    public static LoginMember 회원검증(Long id){
        return new LoginMember(id);
    }

    public static AuthToken 토큰(){
        return AuthToken.builder()
                .accessToken(엑세스_토큰)
                .refreshToken(리프레시_토큰)
                .build();
    }

    public static TokenRenewalRequest 리프레쉬_토큰_발급_DTO(){
        return new TokenRenewalRequest(리프레시_토큰);
    }

    public static AccessTokenResponse 리프레쉬_토큰_응답_DTO(){
        return new AccessTokenResponse(리뉴얼_엑세스_토큰);
    }

    public static AccessAndRefreshTokenResponse 로그인_응답_DTO(){
        return AccessAndRefreshTokenResponse.of(토큰());
    }
}
