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
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String username, String password, String name, String provider, String providerId, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public void passwordUpdate(String password){
        this.password = password;
    }
}
