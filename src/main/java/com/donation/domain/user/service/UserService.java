package com.donation.domain.user.service;

import com.donation.domain.user.dto.UserEmailRespDto;
import com.donation.domain.user.dto.UserPasswordModifyReqDto;
import com.donation.domain.user.dto.UserProfileUpdateReqDto;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.domain.user.entity.User;
import com.donation.domain.user.repository.UserRepository;
import com.donation.infrastructure.Image.AwsS3Service;
import com.donation.infrastructure.support.AuthEncoder;
import com.donation.infrastructure.util.PageCustom;
import com.donation.presentation.auth.LoginMember;
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
    private final AuthEncoder authEncoder;

    public UserEmailRespDto checkUniqueEmail(String email){
        return UserEmailRespDto.of(userRepository.existsByEmail(email));
    }

    @Transactional
    public void passwordModify(LoginMember loginMember, UserPasswordModifyReqDto userPasswordModifyReqDto){
        User user = userRepository.getById(loginMember.getId());
        authEncoder.compare(userPasswordModifyReqDto.getCurrentPassword(), user.getPassword());
        user.changeNewPassword(authEncoder.encode(userPasswordModifyReqDto.getModifyPassword()));
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
