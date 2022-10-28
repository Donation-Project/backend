package com.donation.domain.entites;

import com.donation.domain.enums.Role;
import com.donation.exception.DonationInvalidateException;
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
    private String provider;
    private String providerId;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String metamask;

    @Builder
    public User(Long id, String email, String password, String name, String profileImage, String provider, String providerId, Role role, String metamask) {
        validateEmail(email);

        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.provider = provider;
        this.providerId = providerId;
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
}
