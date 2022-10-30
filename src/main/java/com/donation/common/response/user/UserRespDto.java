package com.donation.common.response.user;

import com.donation.domain.entites.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UserRespDto {
    private Long id;
    private String email;
    private String name;
    private String profileImage;
    private String metamask;


    @Builder
    @QueryProjection
    public UserRespDto(Long id, String email, String name, String profileImage, String metamask) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profileImage = profileImage;
        this.metamask = metamask;
    }

    public static UserRespDto of(User user) {
        return UserRespDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .metamask(user.getMetamask())
                .build();
    }

}
