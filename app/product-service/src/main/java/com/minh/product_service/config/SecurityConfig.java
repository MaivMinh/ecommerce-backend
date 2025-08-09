package com.minh.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .anyRequest().permitAll();
        });
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    public String[] getOpenAPIDoc() {
        List<String> lstUrl = new ArrayList<>();
        lstUrl.add("/swagger-ui/**");
        lstUrl.add("/v3/api-docs/**");
        lstUrl.add("/swagger-ui/index.html");
        lstUrl.add("/webjars/**");
        lstUrl.add("/api/v1/recruitment/position/**");
        lstUrl.add("/api/v1/recruitment/position-class/**");
        lstUrl.add("/api/v1/recruitment/position-group/**");
        lstUrl.add("/api/v1/recruitment/location/**");
        lstUrl.add("/api/v1/recruitment/job-ad/**");
        lstUrl.add("/api/v1/recruitment/candidate/apply");
        lstUrl.add("/api/v1/recruitment/auth/login");
        return lstUrl.toArray(new String[0]);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("http://localhost:8080") // only API Gateway.
                        .allowCredentials(false)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .maxAge(3600)
                        .exposedHeaders("*");
            }
        };
    }
}
