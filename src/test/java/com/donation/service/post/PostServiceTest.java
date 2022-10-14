package com.donation.service.post;

import com.donation.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;


    @Autowired
    private UserRepository userRepository;



}
