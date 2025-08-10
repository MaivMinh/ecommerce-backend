package com.minh.product_service.query.projection;

import com.minh.product_service.command.events.ProductCreatedEvent;
import com.minh.product_service.query.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup(value = "product-group")
public class ProductProjection {
    private final ProductService productService;

    @EventHandler
    public void on(ProductCreatedEvent event) {
        productService.createProduct(event);
    }
}
