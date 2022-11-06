package com.donation.common;

import com.donation.common.request.auth.TokenRenewalRequest;
import com.donation.common.response.auth.AccessAndRefreshTokenResponse;
import com.donation.common.response.auth.AccessTokenResponse;
import com.donation.service.auth.domain.AuthToken;

public class AuthFixtures {


    public static final String 엑세스_토큰 = "aaaaa.bbbbb.ccccc";
    public static final String 리프레시_토큰 = "ccccc.bbbbb.aaaaa";
    public static final String 리뉴얼_엑세스_토큰 = "bbbbb.ccccc.aaaaa";
    public static final String 토큰_정보 = "Bearer " + 엑세스_토큰;
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

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
