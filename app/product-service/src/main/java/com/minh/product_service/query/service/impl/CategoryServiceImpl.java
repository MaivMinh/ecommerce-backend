package com.minh.product_service.query.service.impl;

import com.minh.common.constants.ErrorCode;
import com.minh.common.exception.BusinessException;
import com.minh.common.exception.ResourceNotFoundException;
import com.minh.common.message.MessageCommon;
import com.minh.product_service.dto.CategoryDTO;
import com.minh.product_service.entity.Category;
import com.minh.product_service.query.queries.FindAllCategoriesQuery;
import com.minh.product_service.query.service.CategoryService;
import com.minh.product_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;


    @Override
    public CategoryDTO getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        HttpStatus.NOT_FOUND.value(),
                        messageCommon.getMessage(ErrorCode.Category.NOT_FOUND, categoryId)
                )
        );
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public List<CategoryDTO> findAllCategories(FindAllCategoriesQuery query) {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }
}
