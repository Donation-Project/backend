package com.donation.common;


import com.donation.common.request.user.UserLoginReqDto;
import com.donation.common.request.user.UserPasswordModifyReqDto;
import com.donation.common.request.user.UserProfileUpdateReqDto;
import com.donation.common.request.user.UserSaveReqDto;
import com.donation.common.response.user.UserRespDto;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UserFixtures {

    /* 일반 사용자 */
    public static final String 일반_사용자_이메일 = "default@email.com";
    public static final String 일반_사용자_패스워드 = "default";
    public static final String 일반_사용자_이름 = "일반 사용자";
    public static final String 일반_사용자_프로필 = "https://avatars.githubusercontent.com/u/106054507?v=4";
    public static final Role 일반_사용자_권한 = Role.USER;
    public static final String 일반_사용자_메타마스크_주소 = "https://dafaultmetamask.com";

    /* 회원가입 데이터 */
    public static UserSaveReqDto 유저_회원가입_DTO = new UserSaveReqDto(일반_사용자_이메일, 일반_사용자_이름, 일반_사용자_패스워드, 일반_사용자_메타마스크_주소);
    /* 로그인 데이터 */
    public static UserLoginReqDto 유저_로그인_DTO = new UserLoginReqDto(일반_사용자_이메일, 일반_사용자_패스워드);
    /* 업데이트 데이터 */
    public static final String 새로운_이미지 = "dwqdqwwd/d/wq/d/qw/d/wqdwqdqw/d/wq/d/q/d/d/qw/d/q/wdqdqwqwd/";

    public static UserProfileUpdateReqDto 유저_프로필_업데이트_DTO = new UserProfileUpdateReqDto(새로운_이미지);

    /* 비밀번호 변경 데이터 */
    public static final String 새로운_일반_사용자_패스워드 = "modifyPassword";
    public static UserPasswordModifyReqDto 비밀번호_변경_DTO(final String currentPassword, final String modifyPassword){
        return new UserPasswordModifyReqDto(currentPassword, modifyPassword);
    }

    /* 단건조회시 반횐되는 데이터 */
    public static UserRespDto 일반_반환_데이터(Long id){
        return UserRespDto.builder()
                .id(id)
                .email(일반_사용자_이메일)
                .name(일반_사용자_이름)
                .profileImage(일반_사용자_프로필)
                .metamask(일반_사용자_메타마스크_주소)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .email(일반_사용자_이메일)
                .password(일반_사용자_패스워드)
                .name(일반_사용자_이름)
                .role(일반_사용자_권한)
                .profileImage(일반_사용자_프로필)
                .metamask(일반_사용자_메타마스크_주소)
                .build();
    }

    public static User createUser(Long id) {
        return User.builder()
                .id(id)
                .email(일반_사용자_이메일)
                .password(일반_사용자_패스워드)
                .name(일반_사용자_이름)
                .role(일반_사용자_권한)
                .profileImage(일반_사용자_프로필)
                .metamask(일반_사용자_메타마스크_주소)
                .build();
    }


    public static User createUser(String email) {
        return User.builder()
                .email(email)
                .password(일반_사용자_패스워드)
                .name(일반_사용자_이름)
                .role(일반_사용자_권한)
                .profileImage(일반_사용자_프로필)
                .metamask(일반_사용자_메타마스크_주소)
                .build();
    }

    public static List<User> creatUserList(int startNum, int lastNum){
        return IntStream.range(startNum, lastNum)
                .mapToObj(i -> createUser("default" + i + "@email.com"))
                .collect(Collectors.toList());
    }
}
