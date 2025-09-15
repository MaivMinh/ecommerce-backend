package com.minh.common.DTOs;

import lombok.Data;

@Data
public class SearchDTO {
    private int page = 0;
    private int size = 10;
    private String sortBy;
    private String sortDirection;
}