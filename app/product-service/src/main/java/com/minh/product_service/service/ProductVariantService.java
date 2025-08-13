package com.minh.product_service.service;

import com.minh.product_service.dto.ProductVariantDTO;

import java.util.List;

public interface ProductVariantService {
    void createProductVariant(ProductVariantDTO productVariantDTO);


    List<ProductVariantDTO> findProductVariantsByProductId(String id);

    void updateProductVariant(ProductVariantDTO productVariantDTO);

    void deleteProductVariant(String id);
}
