package com.donation.service.user;

import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.exception.DonationInvalidateException;
import com.donation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConstConfig config;

    @Transactional
    public Long save(UserSaveReqDto userSaveReqDto) {
        User user = validateMember(userSaveReqDto);
        return userRepository.save(user).getId();
    }

    public User validateMember(UserSaveReqDto userSaveReqDto){
        userRepository.validateExistsByEmail(userSaveReqDto.getEmail());
        userSaveReqDto.setPassword(passwordEncoder.encode(userSaveReqDto.getPassword()));
        return userSaveReqDto.toUser(config.getBasicImageProfile());
    }

    public User login(UserLoginReqDto userLoginReqDto){
        return validateLogin(userLoginReqDto);
    }

    public User validateLogin(UserLoginReqDto userLoginReqDto) {
        User user = userRepository.getByEmail(userLoginReqDto.getEmail());
        if(passwordEncoder.matches(userLoginReqDto.getPassword(), user.getPassword())){
            return user;
        }
        throw new DonationInvalidateException("패스워드가 일치하지 않습니다.");
    }
}
