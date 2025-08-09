package com.minh.product_service.command.events;

import lombok.Data;

@Data
public class ProductCreatedEvent {
    private String parentId;
    private String name;
    private String description;
    private String slug;
    private String image;
}
