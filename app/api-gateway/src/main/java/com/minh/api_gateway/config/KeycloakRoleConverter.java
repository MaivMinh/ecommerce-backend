package com.minh.api_gateway.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakRoleConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<String> roles = extractResourceRoles(jwt);

        var authorities = roles.stream()
                .map(role -> "ROLE_" + role) // Bắt buộc ROLE_ prefix để .hasRole() hoạt động
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities, getUsername(jwt));
    }

    private Collection<String> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return Collections.emptySet();

        Map<String, Object> client = (Map<String, Object>) resourceAccess.get("e-commerce");
        if (client == null) return Collections.emptySet();

        Collection<String> roles = (Collection<String>) client.get("roles");
        return roles != null ? roles : Collections.emptySet();
    }

    private String getUsername(Jwt jwt) {
        return jwt.getClaimAsString("preferred_username");
    }
}
