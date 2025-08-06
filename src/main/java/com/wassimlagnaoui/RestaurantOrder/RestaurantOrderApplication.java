package com.wassimlagnaoui.RestaurantOrder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RestaurantOrderApplication {

	public static void main(String[] args) {

		SpringApplication.run(RestaurantOrderApplication.class, args);

		System.out.println(new BCryptPasswordEncoder().encode("password"));
	}

}
