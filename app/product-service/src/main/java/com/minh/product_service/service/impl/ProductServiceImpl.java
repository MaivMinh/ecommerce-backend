package com.minh.product_service.service.impl;

import com.minh.common.constants.ErrorCode;
import com.minh.common.constants.ResponseMessages;
import com.minh.common.enums.ProductStatus;
import com.minh.common.message.MessageCommon;
import com.minh.common.response.ResponseData;
import com.minh.product_service.command.events.ProductCreatedEvent;
import com.minh.product_service.command.events.ProductDeletedEvent;
import com.minh.product_service.command.events.ProductUpdatedEvent;
import com.minh.product_service.dto.ProductDTO;
import com.minh.product_service.dto.ProductImageDTO;
import com.minh.product_service.dto.ProductVariantDTO;
import com.minh.product_service.entity.Product;
import com.minh.product_service.query.queries.FindAllProductsQuery;
import com.minh.product_service.query.queries.FindProductByIdQuery;
import com.minh.product_service.query.queries.FindProductBySlugQuery;
import com.minh.product_service.repository.ProductRepository;
import com.minh.product_service.service.ProductImageService;
import com.minh.product_service.service.ProductService;
import com.minh.product_service.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;
    private final ProductVariantService productVariantService;
    private final ProductImageService productImageService;

    private static String generateSlug(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        // Convert to lowercase
        String slug = name.toLowerCase();
        // Handle Vietnamese accents and other diacritical marks
        slug = java.text.Normalizer.normalize(slug, java.text.Normalizer.Form.NFD);
        // Replace spaces with hyphens
        slug = slug.replaceAll("\\s+", "-");
        // Remove special characters but keep normalized characters
        slug = slug.replaceAll("[^\\p{ASCII}]", "");
        slug = slug.replaceAll("[^a-z0-9-]", "");
        // Replace multiple hyphens with a single one
        slug = slug.replaceAll("-+", "-");
        // Remove leading and trailing hyphens
        slug = slug.replaceAll("^-|-$", "");
        return slug;
    }

    @Override
    public ResponseData findAllProducts(FindAllProductsQuery query) {
        Pageable pageable = PageRequest.of(query.getPage(), query.getSize());
        Page<Product> page = productRepository.findAll(pageable);
        List<ProductDTO> productDTOS = page
                .stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        for (ProductDTO productDTO : productDTOS) {
            /// get product variants.
            List<ProductVariantDTO> productVariantDTOS = productVariantService.findProductVariantsByProductId(productDTO.getId());
            productDTO.setProductVariants(new ArrayList<>(productVariantDTOS));

            /// get product images.
            List<String> imageUrls = productImageService.findProductImagesByProductId(productDTO.getId())
                    .stream()
                    .map(ProductImageDTO::getImageUrl)
                    .collect(Collectors.toList());
            productDTO.setImages(imageUrls);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("page", page.getNumber() + 1);
        payload.put("size", page.getSize());
        payload.put("totalElements", page.getTotalElements());
        payload.put("totalPages", page.getTotalPages());
        payload.put("products", productDTOS);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .data(payload)
                .build();
    }

    @Override
    public ResponseData findProductById(FindProductByIdQuery query) {
        Product product = productRepository.findById(query.getProductId()).orElse(null);
        if (product == null) {
            return ResponseData.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(messageCommon.getMessage(ErrorCode.Product.NOT_FOUND, query.getProductId()))
                    .build();
        }

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .data(productDTO)
                .build();
    }

    @Override
    public ResponseData findProductBySlug(FindProductBySlugQuery query) {
        Product product = productRepository.findBySlug(query.getSlug());
        if (product == null) {
            return ResponseData.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(messageCommon.getMessage(ErrorCode.Product.NOT_FOUND, query.getSlug()))
                    .build();
        }

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .data(productDTO)
                .build();
    }

    @Override
    public void createProduct(ProductCreatedEvent event) {
        Product product = modelMapper.map(event, Product.class);
        product.setSlug(generateSlug(product.getName()));
        product.setStatus(ProductStatus.valueOf(event.getStatus()));
        productRepository.save(product);

        /// Tạo mới các biến thể liên quan đến sản phẩm.
        if (!CollectionUtils.isEmpty(event.getProductVariants())) {
            event.getProductVariants().forEach(productVariantDTO -> {
                productVariantDTO.setProductId(product.getId());
                productVariantService.createProductVariant(productVariantDTO);
            });
        }

        /// Lưu danh sách hình ảnh.
        if (!CollectionUtils.isEmpty(event.getImages())) {
            event.getImages().forEach(image -> {
                ProductImageDTO productImageDTO = new ProductImageDTO();
                productImageDTO.setId(UUID.randomUUID().toString());
                productImageDTO.setProductId(product.getId());
                productImageDTO.setImageUrl(image);
                productImageService.createProductImage(productImageDTO);
            });
        }
    }

    @Override
    public void updateProduct(ProductUpdatedEvent event) {
        Product product = productRepository.findById(event.getId()).orElse(null);
        if (product == null) {
            throw new RuntimeException(messageCommon.getMessage(ErrorCode.Product.NOT_FOUND, event.getId()));
        }
        modelMapper.map(event, product);
        productRepository.save(product);

        /// Cập nhật các biến thể liên quan đến sản phẩm.
//        if (!CollectionUtils.isEmpty(event.getProductVariants())) {
//            event.getProductVariants().forEach(productVariantDTO -> {
//                productVariantDTO.setProductId(product.getId());
//                if (productVariantDTO.getId() == null) {
//                    productVariantService.createProductVariant(productVariantDTO);
//                } else {
//                    productVariantService.updateProductVariant(productVariantDTO);
//                }
//            });
//        }

        List<ProductVariantDTO> productVariantDTOS = event.getProductVariants();
        Map<String, ProductVariantDTO> variantMap = productVariantDTOS.stream()
                .collect(Collectors.toMap(ProductVariantDTO::getId, variant -> variant));

        /// Nếu một biến thể không còn trong danh sách mới, xoá nó.
        List<ProductVariantDTO> existingVariants = productVariantService.findProductVariantsByProductId(product.getId());
        existingVariants.forEach(productVariantDTO -> {
            if (!variantMap.containsKey(productVariantDTO.getId())) {
                productVariantService.deleteProductVariant(productVariantDTO.getId());
            } else {
                // Cập nhật biến thể nếu tồn tại trong danh sách mới.
                ProductVariantDTO updatedVariant = variantMap.get(productVariantDTO.getId());
                updatedVariant.setProductId(product.getId());
                productVariantService.updateProductVariant(updatedVariant);
            }
        });

        /// Duyệt danh sách biến thể mới và tạo mới nếu chưa tồn tại.
        for (ProductVariantDTO newVariant : productVariantDTOS) {
            if (newVariant.getId() == null || newVariant.getId().isEmpty()) {
                newVariant.setProductId(product.getId());
                productVariantService.createProductVariant(newVariant);
            }
        }

        /// Cập nhật lại danh sách hình ảnh.
        if (!CollectionUtils.isEmpty(event.getImages())) {
            // Xoá tất cả hình ảnh cũ liên quan đến sản phẩm.
            List<ProductImageDTO> existingImages = productImageService.findProductImagesByProductId(product.getId());
            existingImages.forEach(image -> productImageService.deleteProductImage(image.getId()));

            // Lưu lại các hình ảnh mới.
            event.getImages().forEach(image -> {
                ProductImageDTO productImageDTO = new ProductImageDTO();
                productImageDTO.setId(UUID.randomUUID().toString());
                productImageDTO.setProductId(product.getId());
                productImageDTO.setImageUrl(image);
                productImageService.createProductImage(productImageDTO);
            });
        }
    }

    @Override
    public void deleteProduct(ProductDeletedEvent event) {
        Product product = productRepository.findById(event.getId()).orElse(null);
        if (product == null) {
            throw new RuntimeException(messageCommon.getMessage(ErrorCode.Product.NOT_FOUND, event.getId()));
        }
        productRepository.delete(product);
    }
}
