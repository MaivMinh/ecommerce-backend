package com.minh.common.utils;

import com.minh.common.DTOs.SearchDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AppUtils {
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication) && authentication.isAuthenticated()) {
            return authentication.getName();
        }

        return null;
    }

    public static Pageable toPageable(SearchDTO searchDTO) {
        int page = Math.max(0, searchDTO.getPage());   // đảm bảo không âm
        int size = Math.max(1, searchDTO.getSize());   // đảm bảo size >= 1
        return PageRequest.of(page, size);
    }
}
