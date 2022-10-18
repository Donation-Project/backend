package com.donation.web.controller.Admin;

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
    public ResponseEntity<?> getAdminPostList(Pageable pageable, @PathVariable PostState... state) {
        log.info(state[0].toString()+state[1].toString());
        Slice<PostListRespDto> list = postService.getAdminPostList(pageable,state);
        return ResponseEntity.ok(CommonResponse.success(list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> confirm(
            @RequestParam PostState postState,
            @PathVariable Long id){
        postService.checkingPost(postState, id);
        return new ResponseEntity<>(CommonResponse.success(), HttpStatus.OK);
    }


}
