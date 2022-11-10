package com.donation.common;

import com.donation.domain.favorite.entity.Favorites;
import com.donation.domain.post.entity.Post;
import com.donation.domain.user.entity.User;

public class FavoriteFixtures {
    public static Favorites createFavorites(User user, Post post) {
        return Favorites.builder()
                .user(user)
                .post(post)
                .build();
    }
}
