package com.donation.presentation;

import com.donation.presentation.auth.LoginInfo;
import com.donation.presentation.auth.LoginMember;
import com.donation.infrastructure.common.CommonResponse;
import com.donation.domain.post.dto.PostSaveReqDto;
import com.donation.domain.post.dto.PostUpdateReqDto;
import com.donation.domain.post.dto.PostFindRespDto;
import com.donation.domain.post.dto.PostListRespDto;
import com.donation.domain.post.dto.PostSaveRespDto;
import com.donation.infrastructure.support.PageCustom;
import com.donation.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donation.domain.post.entity.PostState.APPROVAL;
import static com.donation.domain.post.entity.PostState.COMPLETION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<?> save(
            @LoginInfo LoginMember loginMember,
            @RequestBody @Valid PostSaveReqDto postSaveReqDto
    ){
        PostSaveRespDto post = postService.createPost(postSaveReqDto, loginMember);
        return new ResponseEntity<>(CommonResponse.success(post), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        PostFindRespDto post = postService.findById(id);
        return ResponseEntity.ok(CommonResponse.success(post));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @LoginInfo LoginMember loginMember,
            @RequestBody @Valid PostUpdateReqDto postUpdateReqDto,
            @PathVariable Long id){
        postService.update(postUpdateReqDto, loginMember, id);
        return ResponseEntity.ok(CommonResponse.success());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @LoginInfo LoginMember loginMember){
        postService.delete(id, loginMember);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @GetMapping
    public ResponseEntity<?> getPostList(Pageable pageable){
        PageCustom<PostListRespDto> list = postService.getList(pageable, APPROVAL, COMPLETION);
        return ResponseEntity.ok(CommonResponse.success(list));
    }

    @GetMapping("/my-page")
    public ResponseEntity<?> getMyPostList(@LoginInfo LoginMember loginMember, Pageable pageable) {
        PageCustom<PostListRespDto> list = postService.getUserIdList(loginMember, pageable);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
}
