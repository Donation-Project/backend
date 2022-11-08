package com.donation.service.user;

import com.donation.auth.LoginMember;
import com.donation.common.request.user.UserProfileUpdateReqDto;
import com.donation.common.response.user.UserEmailRespDto;
import com.donation.common.response.user.UserRespDto;
import com.donation.domain.entites.User;
import com.donation.repository.user.UserRepository;
import com.donation.repository.utils.PageCustom;
import com.donation.service.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description 회원 정보 CRUD 서비스
 * @author  정우진
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public UserEmailRespDto checkUniqueEmail(String email){
        return UserEmailRespDto.of(userRepository.existsByEmail(email));
    }

    public UserRespDto findById(LoginMember loginMember){
        return UserRespDto.of(userRepository.getById(loginMember.getId()));
    }

    @Transactional
    public void updateProfile(LoginMember loginMember, UserProfileUpdateReqDto userProfileUpdateReqDto){
        User user = userRepository.getById(loginMember.getId());
        user.changeNewProfileImage(awsS3Service.upload(userProfileUpdateReqDto.getProfileImage()));
    }

    public PageCustom<UserRespDto> getList(Pageable pageable){
        return userRepository.getPageDtoList(pageable);
    }
}
