package com.minibank.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MinibankEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinibankEurekaApplication.class, args);
	}

}
