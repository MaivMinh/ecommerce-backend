package com.minh.order_service.query.controller;

import com.minh.common.DTOs.SearchDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchOrdersRequest extends SearchDTO {
    private String orderId;
    private String username;
    private String status;
}