package com.minh.product_service.query.handler;

import com.minh.common.response.ResponseData;
import com.minh.product_service.query.queries.*;
import com.minh.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductQueryHandler {
    private final ProductService productService;

    @QueryHandler
    public ResponseData handle(FindProductByIdQuery query) {
        return productService.findProductById(query);
    }

    @QueryHandler
    public ResponseData handle(FindProductBySlugQuery query) {
        return productService.findProductBySlug(query);
    }

    @QueryHandler
    public ResponseData handle(FindAllProductsQuery query) {
        return productService.findAllProducts(query);
    }
}