package com.donation.domain.auth.application;

import com.donation.domain.auth.dto.AccessAndRefreshTokenResponse;
import com.donation.domain.auth.dto.AccessTokenResponse;
import com.donation.domain.auth.dto.TokenRenewalRequest;
import com.donation.domain.auth.entity.AuthToken;
import com.donation.domain.user.dto.UserLoginReqDto;
import com.donation.domain.user.dto.UserSaveReqDto;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.infrastructure.support.AuthEncoder;
import lombok.RequiredArgsConstructor;
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

    private final AuthEncoder authEncoder;

    @Transactional
    public Long save(final UserSaveReqDto userSaveReqDto) {
        User user = validateMember(userSaveReqDto);
        return userRepository.save(user).getId();
    }

    public User validateMember(final UserSaveReqDto userSaveReqDto){
        userRepository.validateExistsByEmail(userSaveReqDto.getEmail());
        userSaveReqDto.setPassword(authEncoder.encode(userSaveReqDto.getPassword()));
        return userSaveReqDto.toUser(profileImageUrl);
    }

    public AccessAndRefreshTokenResponse login(final UserLoginReqDto userLoginReqDto){
        User foundMember = validateLogin(userLoginReqDto);
        AuthToken authToken = tokenCreator.createAuthToken(foundMember.getId());
        return AccessAndRefreshTokenResponse.of(authToken);
    }

    public User validateLogin(final UserLoginReqDto userLoginReqDto) {
        User user = userRepository.getByEmail(userLoginReqDto.getEmail());
        authEncoder.compare(userLoginReqDto.getPassword(), user.getPassword());
        return user;
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
