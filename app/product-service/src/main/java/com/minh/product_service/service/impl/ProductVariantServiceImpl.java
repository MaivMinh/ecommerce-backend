package com.minh.product_service.service.impl;

import com.minh.product_service.dto.ProductVariantDTO;
import com.minh.product_service.entity.ProductVariant;
import com.minh.product_service.repository.ProductVariantRepository;
import com.minh.product_service.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createProductVariant(ProductVariantDTO productVariantDTO) {
        ProductVariant productVariant = new ProductVariant();
        modelMapper.map(productVariantDTO, productVariant);
        productVariant.setId(UUID.randomUUID().toString());
        productVariantRepository.save(productVariant);
    }

    @Override
    public List<ProductVariantDTO> findProductVariantsByProductId(String id) {
        List<ProductVariant> productVariants = productVariantRepository.findAllByProductId(id);
        if (productVariants.isEmpty()) {
            return List.of();
        }
        return productVariants.stream()
                .map(variant -> modelMapper.map(variant, ProductVariantDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductVariant(ProductVariantDTO productVariantDTO) {
        ProductVariant productVariant = productVariantRepository.findById(productVariantDTO.getId()).orElse(null);
        if (Objects.isNull(productVariant)) {
            throw new IllegalArgumentException("Product variant not found with id: " + productVariantDTO.getId());
        }
        modelMapper.map(productVariantDTO, productVariant);
        productVariantRepository.save(productVariant);
    }

    @Override
    public void deleteProductVariant(String id) {
        productVariantRepository.deleteById(id);
    }

    @Override
    public List<ProductVariantDTO> findProductVariantsByProductIds(List<String> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            return new ArrayList<>();
        }

        return productVariantRepository.findAllByProductIdIn(productIds).stream()
                .map(variant -> modelMapper.map(variant, ProductVariantDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Hàm thực hiện lấy danh sách các biến thể sản phẩm dựa trên danh sách ID của chúng.
     * @param productVariantIds
     * @return List<ProductVariantDTO>
     */
    @Override
    public List<ProductVariantDTO> findProductVariantsByIds(List<String> productVariantIds) {
        if (CollectionUtils.isEmpty(productVariantIds)) {
            return new ArrayList<>();
        }

        List<ProductVariant> productVariants = productVariantRepository.findProductVariantsByIds(productVariantIds);
        return productVariants.stream()
                .map(variant -> modelMapper.map(variant, ProductVariantDTO.class))
                .collect(Collectors.toList());
    }
}
