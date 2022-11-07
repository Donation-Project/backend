package com.donation.common;

import com.donation.domain.entites.Favorites;
import com.donation.domain.entites.Post;
import com.donation.domain.entites.User;

public class FavoriteFixtures {
    public static Favorites createFavorites(User user, Post post) {
        return Favorites.builder()
                .user(user)
                .post(post)
                .build();
    }
}
