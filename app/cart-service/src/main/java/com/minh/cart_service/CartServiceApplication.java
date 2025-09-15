package com.minh.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableJpaRepositories(basePackages = "com.minh.cart_service.repository")
@EntityScan(value = "com.minh.cart_service.entity")
@ComponentScan(basePackages = {"com.minh.*"})
@EnableMethodSecurity
public class CartServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}
