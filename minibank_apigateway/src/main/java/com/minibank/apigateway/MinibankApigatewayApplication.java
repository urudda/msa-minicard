package com.minibank.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MinibankApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinibankApigatewayApplication.class, args);
	}

}
