package com.donation.service.post;

import com.donation.common.request.post.PostSaveReqDto;
import com.donation.common.request.post.PostUpdateReqDto;
import com.donation.common.response.post.PostFindRespDto;
import com.donation.common.response.post.PostRespDto;
import com.donation.common.response.post.PostSaveRespDto;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;
import com.donation.repository.post.PostRepository;
import com.donation.repository.user.UserRepository;
import com.donation.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.donation.domain.enums.PostState.DELETE;
import static com.donation.domain.enums.PostState.WAITING;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PostSaveRespDto save(PostSaveReqDto postSaveReqDto, Long userid){
        User user = userService.get(userid);
        Post post = postRepository.save(postSaveReqDto.toPost(user));
        return PostSaveRespDto.toDto(post, user);
    }

    public void contentUpdate(PostUpdateReqDto updateReqDto, Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        post.update(updateReqDto);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(IllegalArgumentException::new);
        postRepository.delete(post);
    }

    public PostFindRespDto findById(Long postId, Long userId) {
        PostFindRespDto post = postRepository.findByUserIdOrRoleAdmin(postId, userId)
                .orElse(null);

        if (post == null)
            return findById(postId);

        return post;
    }

    public PostFindRespDto findById(Long postId) {
        Post post = postRepository.findByIdAndStateNotAndStateNot(postId, DELETE, WAITING)
                .orElseThrow(IllegalArgumentException::new);
        return PostFindRespDto.toDto(post, false);
    }

    public Slice<PostRespDto> getList(Pageable pageable) {
        return postRepository.findPageableAll(pageable);
    }


    private User findUser(Long userid) {
        return userRepository.findById(userid)
                .orElseThrow(IllegalArgumentException::new);
    }
}
