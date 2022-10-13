package com.donation.service.user;

import com.donation.common.reponse.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.domain.entites.User;
import com.donation.exception.EmailDuplicateException;
import com.donation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public Long join(UserJoinReqDto userJoinReqDto) {
        User user = userJoinReqDto.toUser();
        if (userRepository.findByUsername(userJoinReqDto.getEmail()).isPresent())
            throw new EmailDuplicateException();

        userRepository.save(user);
        return user.getId();
    }

    public void login(UserLoginReqDto userLoginReqDto){
        userRepository.findByUsernameAndPassword(userLoginReqDto.getEmail(), userLoginReqDto.getPassword())
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public UserRespDto get(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return new UserRespDto(user);
    }


    @Transactional(readOnly = true)
    public Slice<UserRespDto> getList(Pageable pageable){
        return userRepository.findPageableAll(pageable);
    }


    public void delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        userRepository.delete(user);
    }

    public void editProfile(Long id, String imageUrl){
        User user = userRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        user.editProfile(imageUrl);
    }
}
