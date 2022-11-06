package com.donation.service.auth.application;

import com.donation.common.request.auth.TokenRenewalRequest;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.common.response.auth.AccessAndRefreshTokenResponse;
import com.donation.common.response.auth.AccessTokenResponse;
import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.user.UserRepository;
import com.donation.service.auth.domain.AuthToken;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    @Value("${profileImageUrl}")
    private String profileImageUrl;
    private final UserRepository userRepository;

    private final TokenCreator tokenCreator;

    @Transactional
    public Long save(final UserSaveReqDto userSaveReqDto) {
        User user = validateMember(userSaveReqDto);
        return userRepository.save(user).getId();
    }

    public User validateMember(final UserSaveReqDto userSaveReqDto){
        userRepository.validateExistsByEmail(userSaveReqDto.getEmail());
        userSaveReqDto.setPassword(BCrypt.hashpw(userSaveReqDto.getPassword(), BCrypt.gensalt()));
        return userSaveReqDto.toUser(profileImageUrl);
    }

    public AccessAndRefreshTokenResponse login(final UserLoginReqDto userLoginReqDto){
        User foundMember = validateLogin(userLoginReqDto);
        AuthToken authToken = tokenCreator.createAuthToken(foundMember.getId());
        return AccessAndRefreshTokenResponse.of(authToken);
    }

    public User validateLogin(final UserLoginReqDto userLoginReqDto) {
        User user = userRepository.getByEmail(userLoginReqDto.getEmail());
        if(BCrypt.checkpw(userLoginReqDto.getPassword(), user.getPassword())){
            return user;
        }
        throw new DonationInvalidateException("패스워드가 일치하지 않습니다.");
    }

    public AccessTokenResponse renewalToken(final TokenRenewalRequest tokenRenewalRequest) {
        String refreshToken = tokenRenewalRequest.getRefreshToken();
        AuthToken authToken = tokenCreator.renewAuthToken(refreshToken);
        return new AccessTokenResponse(authToken.getAccessToken());
    }

    public Long extractMemberId(final String accessToken) {
        Long memberId = tokenCreator.extractPayload(accessToken);
        userRepository.validateExistsById(memberId);
        return memberId;
    }
}
