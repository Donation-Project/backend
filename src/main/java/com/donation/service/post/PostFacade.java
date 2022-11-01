package com.donation.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;

    public void increase(final Long id, final float amount) throws InterruptedException {
        while (true){
            try {
                postService.increase(id, amount);
                break;
            }catch (Exception e){
                Thread.sleep(50);
            }
        }
    }
}
