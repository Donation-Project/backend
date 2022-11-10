package com.donation.presentation;

import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.entity.PostState;
import com.donation.infrastructure.support.PageCustom;
import com.donation.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    private final PostService postService;

    @GetMapping("/{state}")
    public ResponseEntity<?> getPostList(@PathVariable PostState[] state, Pageable pageable) {
        PageCustom<PostListRespDto> list = postService.getList(pageable, state);
        return ResponseEntity.ok(CommonResponse.success(list));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> confirm(
            @RequestParam PostState postState,
            @PathVariable Long id) {
        postService.confirm(postState, id);
        return ResponseEntity.ok(CommonResponse.success());
    }


}
