package com.minibank.b2bt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class B2BTransferApplication {

	public static void main(String[] args) {
		SpringApplication.run(B2BTransferApplication.class, args);
	}
}
