package com.donation.domain.favorite.entity;

import com.donation.infrastructure.embed.BaseEntity;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Table(name = "favorites")
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Favorites extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "favorite_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public Favorites(Long id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public static Favorites of(User user){
       return Favorites.builder()
                .user(user)
                .build();
    }
}
