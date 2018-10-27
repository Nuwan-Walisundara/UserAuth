package com.user.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBoot	-	ApplicationClass
 */
@SpringBootApplication(scanBasePackages={"com.user.dao", "com.user.service","com.user.controller","com.user.util"})
public class Application {
	
	/**
	 * This one start the springBootApplication
	 *
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}