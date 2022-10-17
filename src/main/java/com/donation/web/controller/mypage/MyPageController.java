package com.donation.web.controller.mypage;

import com.donation.common.CommonResponse;
import com.donation.common.response.post.PostListRespDto;
import com.donation.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/MyPage")
public class MyPageController {
    private final PostRepository postRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getListPost(@PathVariable Long id, Pageable pageable) {
        Slice<PostListRespDto> allUserId = postRepository.findAllUserId(id, pageable);
        return ResponseEntity.ok(CommonResponse.success(allUserId));
    }
}
