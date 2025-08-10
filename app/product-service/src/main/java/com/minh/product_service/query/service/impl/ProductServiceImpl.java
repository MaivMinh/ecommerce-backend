package com.minh.product_service.query.service.impl;

import com.minh.product_service.command.events.ProductCreatedEvent;
import com.minh.product_service.entity.Product;
import com.minh.product_service.query.service.ProductService;
import com.minh.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    @Override
    public void createProduct(ProductCreatedEvent event) {
        Product product = modelMapper.map(event, Product.class);
        productRepository.save(product);
    }
}
