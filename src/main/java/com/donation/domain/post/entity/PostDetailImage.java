package com.donation.domain.post.entity;

import com.donation.infrastructure.embed.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name="post_detail_image")
@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PostDetailImage extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_detail_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imagePath;

    @Builder
    public PostDetailImage(Long id, Post post, String imagePath) {
        this.id = id;
        this.post = post;
        this.imagePath = imagePath;
    }

    public static PostDetailImage of(String imagePath){
        return PostDetailImage.builder()
                .imagePath(imagePath)
                .build();
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
