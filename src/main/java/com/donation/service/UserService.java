package com.donation.service;

import com.donation.common.request.LoginReqDto;
import com.donation.domain.entites.User;
import com.donation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void login(LoginReqDto loginReqDto) {
        User user = userRepository.findByUsername(loginReqDto.getEmail())
                .orElseThrow(IllegalArgumentException::new);
    }
}
