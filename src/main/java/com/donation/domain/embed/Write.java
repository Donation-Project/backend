package com.donation.domain.embed;

import com.donation.domain.entites.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Write {
    private String title;

    private String content;

    @Builder
    public Write(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Write(Post post){
        this.title = post.getWrite().getTitle();
        this.content = post.getWrite().getContent();
    }

}