package com.coding.nuerobyte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.codingplatform.openapi.api"})
public class NuerobyteApplication {

	public static void main(String[] args) {
		SpringApplication.run(NuerobyteApplication.class, args);
	}

}
