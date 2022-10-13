package com.donation;

import com.donation.config.ConstConfig;
import com.donation.domain.entites.User;
import com.donation.domain.enums.Role;
import com.donation.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
public class DonationApplication {

	private final UserRepository userRepository;
	private final ConstConfig config;

	public static void main(String[] args) {
		SpringApplication.run(DonationApplication.class, args);
	}


	@PostConstruct
	public void init(){
		//given
		List<User> requestPosts = IntStream.range(1, 31)
				.mapToObj(i -> User.builder()
						.username("username" + i)
						.name("name" + i)
						.password("password" + i)
						.profileImage(config.getBasicImageProfile())
						.role(Role.USER)
						.build()
				)
				.collect(Collectors.toList());

		userRepository.saveAll(requestPosts);

	}
}
