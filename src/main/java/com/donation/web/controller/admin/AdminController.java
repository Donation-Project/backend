package com.donation.web.controller.admin;

import com.donation.common.CommonResponse;
import com.donation.common.response.post.PostListRespDto;
import com.donation.domain.enums.PostState;
import com.donation.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
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
        Slice<PostListRespDto> list = postService.getList(pageable, state);
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
