package com.donation.service.user;

import com.donation.common.response.user.UserRespDto;
import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.exception.user.EmailDuplicateException;
import com.donation.exception.NoSuchElementException;
import com.donation.repository.user.UserRepository;
import com.donation.service.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description 회원 정보 CRUD 서비스
 * @author  정우진
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final ConstConfig config;

    public Long join(UserJoinReqDto userJoinReqDto) {
        User user = userJoinReqDto.toUser(config.getBasicImageProfile());
        if (userRepository.findByUsername(userJoinReqDto.getEmail()).isPresent())
            throw new EmailDuplicateException();

        userRepository.save(user);
        return user.getId();
    }

    public void login(UserLoginReqDto userLoginReqDto){
        userRepository.findByUsernameAndPassword(userLoginReqDto.getEmail(), userLoginReqDto.getPassword())
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public User get(Long id){
        return userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public Slice<UserRespDto> getList(Pageable pageable){
        return userRepository.findPageableAll(pageable);
    }

    public void delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        awsS3Service.delete(user.getProfileImage());
        userRepository.delete(user);
    }

    public void editProfile(Long id, MultipartFile multipartFile){
        User user = userRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        user.editProfile(awsS3Service.upload(multipartFile));
    }
}
