package com.donation.common.reponse;

import com.donation.domain.entites.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;


@Getter
public class UserRespDto {
    private Long id;
    private String username;
    private String name;

    private String profileImage;

    @QueryProjection
    public UserRespDto(Long id, String username, String name, String profileImage) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.profileImage = profileImage;
    }
    public UserRespDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.profileImage = user.getProfileImage();
    }
}
