package com.donation.domain.entites;

import com.donation.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor(access = PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "email", unique = true)
    private String username;
    private String password;
    private String name;

    private String profileImage;

    private String provider;

    private String providerId;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String metamask;

    @Builder
    public User(Long id, String username, String password, String name, String profileImage, String provider, String providerId, Role role, String metamask) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.profileImage = profileImage;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
        this.metamask = metamask;
    }

    public void passwordUpdate(String password){
        this.password = password;
    }
    public void editProfile(String imageUrl){
        this.profileImage = imageUrl;
    }

}
