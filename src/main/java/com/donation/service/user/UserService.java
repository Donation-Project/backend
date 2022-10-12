package com.donation.service;

import com.donation.common.reponse.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.domain.entites.User;
import com.donation.exception.EmailDuplicateException;
import com.donation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public void join(UserJoinReqDto userJoinReqDto) {
        User user = userJoinReqDto.toUser();
        if (userRepository.findByUsername(userJoinReqDto.getEmail()).isPresent())
            throw new EmailDuplicateException();

        userRepository.save(user);
    }

    public void login(UserLoginReqDto userLoginReqDto){
        userRepository.findByUsernameAndPassword(userLoginReqDto.getEmail(), userLoginReqDto.getPassword())
                .orElseThrow(IllegalArgumentException::new);
    }

    public UserRespDto get(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return new UserRespDto(user);
    }

    public List<UserRespDto> getList(){
        return userRepository.findAll().stream()
                .map(UserRespDto::new)
                .collect(Collectors.toList());
    }

    public void delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        userRepository.delete(user);
    }
}
