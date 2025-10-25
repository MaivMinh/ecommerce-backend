package com.minh.side_service;

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
@EnableJpaRepositories(basePackages = "com.minh.side_service")
@EntityScan(value = "com.minh.side_service.entity")
@ComponentScan(basePackages = {"com.minh.side_service.*", "com.minh.common"})
@EnableMethodSecurity
public class SideServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SideServiceApplication.class, args);
	}

}
