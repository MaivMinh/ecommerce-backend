package com.minh.product_service.query.service.impl;

import com.minh.common.constants.ErrorCode;
import com.minh.common.constants.ResponseMessages;
import com.minh.common.exception.BusinessException;
import com.minh.common.exception.ResourceNotFoundException;
import com.minh.common.message.MessageCommon;
import com.minh.common.response.ResponseData;
import com.minh.product_service.command.events.CategoryCreatedEvent;
import com.minh.product_service.command.events.CategoryDeletedEvent;
import com.minh.product_service.command.events.CategoryUpdatedEvent;
import com.minh.product_service.dto.CategoryDTO;
import com.minh.product_service.entity.Category;
import com.minh.product_service.query.queries.FindAllCategoriesQuery;
import com.minh.product_service.query.queries.FindCategoryBySlug;
import com.minh.product_service.query.queries.SearchCategoriesByNameQuery;
import com.minh.product_service.query.service.CategoryService;
import com.minh.product_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;


    @Override
    public ResponseData getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return ResponseData.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(messageCommon.getMessage(ErrorCode.Category.NOT_FOUND, categoryId))
                    .build();
        }
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .payload(modelMapper.map(category, CategoryDTO.class))
                .build();
    }

    @Override
    public ResponseData findAllCategories(FindAllCategoriesQuery query) {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .payload(categoryDTOs)
                .build();
    }

    @Override
    public ResponseData findCategoryBySlug(FindCategoryBySlug query) {
        if (!StringUtils.hasText(query.getSlug())) {
            throw new ResourceNotFoundException(
                    HttpStatus.NOT_FOUND.value(),
                    messageCommon.getMessage(ErrorCode.Category.SLUG_NOT_FOUND, query.getSlug())
            );
        }

        Category category = categoryRepository.findBySlug(query.getSlug());
        if (category == null) {
            return ResponseData.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message(messageCommon.getMessage(ErrorCode.Category.SLUG_NOT_FOUND, query.getSlug()))
                    .build();
        }

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .payload(modelMapper.map(category, CategoryDTO.class))
                .build();
    }

    @Override
    public ResponseData searchCategoriesByName(SearchCategoriesByNameQuery query) {
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(query.getName());
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .payload(categoryDTOs)
                .build();
    }

    @Override
    public void createCategory(CategoryCreatedEvent event) {
        Category category = modelMapper.map(event, Category.class);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(CategoryUpdatedEvent event) {
        Category category = categoryRepository.findById(event.getId()).orElseThrow(
                () -> new RuntimeException(
                        messageCommon.getMessage(ErrorCode.Category.NOT_FOUND, event.getId())
                )
        );
        modelMapper.map(event, category);
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(CategoryDeletedEvent event) {
        Category category = categoryRepository.findById(event.getId()).orElseThrow(
                () -> new RuntimeException(messageCommon.getMessage(ErrorCode.Category.NOT_FOUND, event.getId()))
        );
        categoryRepository.delete(category);
    }
}
