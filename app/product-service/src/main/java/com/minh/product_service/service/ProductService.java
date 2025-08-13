package com.minh.product_service.service;

import com.minh.common.response.ResponseData;
import com.minh.product_service.command.events.ProductCreatedEvent;
import com.minh.product_service.command.events.ProductDeletedEvent;
import com.minh.product_service.command.events.ProductUpdatedEvent;
import com.minh.product_service.query.queries.FindAllProductsQuery;
import com.minh.product_service.query.queries.FindProductByIdQuery;
import com.minh.product_service.query.queries.FindProductBySlugQuery;

public interface ProductService {
    ResponseData findAllProducts(FindAllProductsQuery query);

    ResponseData findProductById(FindProductByIdQuery query);

    ResponseData findProductBySlug(FindProductBySlugQuery query);

    void createProduct(ProductCreatedEvent event);

    void updateProduct(ProductUpdatedEvent event);

    void deleteProduct(ProductDeletedEvent event);
}
