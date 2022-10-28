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

    public UserRespDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
        this.metamask = user.getMetamask();
    }

}
