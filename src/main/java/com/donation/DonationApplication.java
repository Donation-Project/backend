package com.donation;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class DonationApplication {


	public static void main(String[] args) {
		SpringApplication.run(DonationApplication.class, args);
	}
}
