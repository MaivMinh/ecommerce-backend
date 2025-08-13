package com.minh.product_service.query.controller;

import com.minh.common.response.ResponseData;
import com.minh.product_service.query.queries.FindAllProductsQuery;
import com.minh.product_service.query.queries.FindProductByIdQuery;
import com.minh.product_service.query.queries.FindProductBySlugQuery;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/api/products")
public class ProductQueryController {
    private final QueryGateway queryGateway;

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ResponseData> findProductById(@PathVariable(value = "productId") String productId) {
        FindProductByIdQuery query = FindProductByIdQuery.builder()
                .productId(productId)
                .build();

        ResponseData response = queryGateway.query(query, ResponseTypes.instanceOf(ResponseData.class)).join();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(value = "/slug")
    public ResponseEntity<ResponseData> findProductBySlug(@RequestParam(value = "slug") String slug) {
        FindProductBySlugQuery query = FindProductBySlugQuery.builder()
                .slug(slug)
                .build();

        ResponseData response = queryGateway.query(query, ResponseTypes.instanceOf(ResponseData.class)).join();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping(value = "")
    public ResponseEntity<ResponseData> findAllProducts(@RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
                                                     HttpServletRequest request) {
        page = (page > 0) ? (page - 1) : 0;
        size = (size > 0) ? size : 10;
        FindAllProductsQuery query = FindAllProductsQuery.builder()
                .page(page)
                .size(size)
                .build();

        /// Send query to Query Bus and wait for result.
        ResponseData response = queryGateway.query(query, ResponseTypes.instanceOf(ResponseData.class)).join();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
