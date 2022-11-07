package com.donation.web.controller.post;

import com.donation.auth.LoginInfo;
import com.donation.auth.LoginMember;
import com.donation.common.CommonResponse;
import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostListRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.repository.utils.PageCustom;
import com.donation.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.donation.domain.enums.PostState.APPROVAL;
import static com.donation.domain.enums.PostState.COMPLETION;

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

    @GetMapping("/{id}/my-page")
    public ResponseEntity<?> getMyPostList(@PathVariable Long id, Pageable pageable) {
        PageCustom<PostListRespDto> list = postService.getUserIdList(id, pageable);
        return ResponseEntity.ok(CommonResponse.success(list));
    }
}
