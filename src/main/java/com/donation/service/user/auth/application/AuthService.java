package com.donation.service.user.auth.application;

import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.user.UserRepository;
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

    @Transactional
    public Long save(UserSaveReqDto userSaveReqDto) {
        User user = validateMember(userSaveReqDto);
        return userRepository.save(user).getId();
    }

    public User validateMember(UserSaveReqDto userSaveReqDto){
        userRepository.validateExistsByEmail(userSaveReqDto.getEmail());
        userSaveReqDto.setPassword(BCrypt.hashpw(userSaveReqDto.getPassword(), BCrypt.gensalt()));
        return userSaveReqDto.toUser(profileImageUrl);
    }

    public User login(UserLoginReqDto userLoginReqDto){
        return validateLogin(userLoginReqDto);
    }

    public User validateLogin(UserLoginReqDto userLoginReqDto) {
        User user = userRepository.getByEmail(userLoginReqDto.getEmail());
        if(BCrypt.checkpw(userLoginReqDto.getPassword(), user.getPassword())){
            return user;
        }
        throw new DonationInvalidateException("패스워드가 일치하지 않습니다.");
    }
}
