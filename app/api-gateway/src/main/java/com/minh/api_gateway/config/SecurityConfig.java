package com.minh.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange(exchange -> {
                    exchange
                            .pathMatchers(this.getOpenAPIDoc()).permitAll()
                            .pathMatchers(HttpMethod.GET).permitAll()
                            .anyExchange().permitAll();
                });
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
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
}
