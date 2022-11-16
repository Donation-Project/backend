package com.donation.domain.user.entity;

import com.donation.infrastructure.embed.BaseEntity;
import com.donation.global.exception.DonationInvalidateException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$");

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", unique = true)
    private String email;
    private String password;
    private String name;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String metamask;

    @Builder
    public User(Long id, String email, String password, String name, String profileImage, Role role, String metamask) {
        validateEmail(email);

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.role = role;
        this.metamask = metamask;
    }

    public void changeNewPassword(final String password){
        this.password = password;
    }
    public void changeNewProfileImage(final String imageUrl){
        this.profileImage = imageUrl;
    }

    private void validateEmail(final String email){
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        if(!matcher.matches()){
            throw new DonationInvalidateException("이메일 형식이 올바르지 않습니다.");
        }
    }
    public void validateAdmin(){
        if(this.role != Role.ADMIN)
            throw new DonationInvalidateException("해당 권한이 없습니다.");
    }
}
