package com.donation.domain.entites;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class PostDetailImage extends BaseEntity{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "postDetailId")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postId")
    private Post post;

    private String imagePath;

    @Builder
    public PostDetailImage(Long id, Post post, String imagePath) {
        this.id = id;
        this.post = post;
        this.imagePath = imagePath;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
