package com.donation.domain.favorite.application;

import com.donation.presentation.auth.LoginMember;
import com.donation.domain.user.dto.UserRespDto;
import com.donation.domain.favorite.entity.Favorites;
import com.donation.domain.favorite.repository.FavoriteRepository;
import com.donation.domain.post.repository.PostRepository;
import com.donation.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.donation.domain.favorite.entity.Favorites.of;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void save(LoginMember loginMember, Long postId){
        favoriteRepository.validateExistsByPostIdAndUserId(postId, loginMember.getId());
        postRepository.getById(postId)
                .addFavorite(of(userRepository.getById(loginMember.getId())));
    }

    public void cancel(LoginMember loginMember, Long postId){
        Favorites favorites = favoriteRepository.getByPostIdAndUserId(postId, loginMember.getId());
        favoriteRepository.delete(favorites);
    }

    public List<UserRespDto> findAll(Long postId){
        List<Long> userId = favoriteRepository.findUserIdByPostId(postId);
        return userRepository.findAllByIdIn(userId).stream()
                .map(UserRespDto::of)
                .collect(Collectors.toList());
    }

    public void deletePostId(Long postId){
        favoriteRepository.deleteAllByPostId(postId);
    }
}
