package com.donation.service.user;

import com.donation.common.request.user.UserJoinReqDto;
import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.response.user.UserRespDto;
import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.exception.DonationDuplicateException;
import com.donation.exception.DonationNotFoundException;
import com.donation.repository.user.UserRepository;
import com.donation.service.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
            throw new DonationDuplicateException(String.format("이미 존재하는 이메일 입니다.(%s)", userJoinReqDto.getEmail()));

        userRepository.save(user);
        return user.getId();
    }

    public User login(UserLoginReqDto userLoginReqDto){
        return userRepository.findByUsernameAndPassword(userLoginReqDto.getEmail(), userLoginReqDto.getPassword())
                .orElseThrow(() -> new DonationNotFoundException("로그인에 실패했습니다."));
    }

    @Transactional(readOnly = true)
    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new DonationNotFoundException("회원을 찾을수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Slice<UserRespDto> getList(Pageable pageable){
        return userRepository.findPageableAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getListIdIn(List<Long> id){
        return userRepository.findAllByIdIn(id);
    }

    public void delete(Long id){
        User user = findById(id);
        awsS3Service.delete(user.getProfileImage());
        userRepository.delete(user);
    }

    public void editProfile(Long id, MultipartFile multipartFile){
        User user = findById(id);
        user.editProfile(awsS3Service.upload(multipartFile));
    }
}
