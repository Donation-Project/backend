package com.donation.presentation;

import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.entity.PostState;
import com.donation.domain.post.service.PostService;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.infrastructure.util.CursorRequest;
import com.donation.infrastructure.util.PageCursor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    private final PostService postService;

    @GetMapping("/{state}")
    public ResponseEntity<?> getPostList(@PathVariable PostState[] state, CursorRequest cursorRequest) {
        PageCursor<PostListRespDto> list = postService.getList(cursorRequest, state);
        return ResponseEntity.ok(CommonResponse.success(list));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> confirm(
            @RequestParam PostState state,
            @PathVariable Long id) {
        postService.confirm(state, id);
        return ResponseEntity.ok(CommonResponse.success());
    }


}
