package com.minh.product_service.service;

import com.minh.product_service.dto.ProductImageDTO;
import com.netflix.spectator.api.Registry;

import java.util.List;

public interface ProductImageService {
    void createProductImage(ProductImageDTO productImageDTO);

    void deleteProductImage(String productImageId);

    void updateProductImage(ProductImageDTO productImageDTO);

    List<ProductImageDTO> findProductImagesByProductId(String id);
}
