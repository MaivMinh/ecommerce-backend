package com.minh.product_service.query.service;


import com.minh.product_service.command.events.ProductCreatedEvent;

public interface ProductService {
    void createProduct(ProductCreatedEvent event);
}
