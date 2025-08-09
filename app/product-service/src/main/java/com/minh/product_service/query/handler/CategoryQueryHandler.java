package com.minh.product_service.query.handler;

import com.minh.common.constants.ResponseMessages;
import com.minh.common.exception.ResourceNotFoundException;
import com.minh.common.response.ResponseData;
import com.minh.product_service.dto.CategoryDTO;
import com.minh.product_service.query.queries.FindAllCategoriesQuery;
import com.minh.product_service.query.queries.FindCategoryByIdQuery;
import com.minh.product_service.query.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryQueryHandler {
    private final CategoryService categoryService;

    @QueryHandler
    public ResponseData handle(FindCategoryByIdQuery findCategoryByIdQuery) throws ResourceNotFoundException {
        String categoryId = findCategoryByIdQuery.getCategoryId();
        CategoryDTO dto = categoryService.getCategoryById(categoryId);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message(ResponseMessages.SUCCESS)
                .payload(dto)
                .build();
    }

    @QueryHandler
    public ResponseData handle(FindAllCategoriesQuery query) {
        List<CategoryDTO> categories = categoryService.findAllCategories(query);
        return ResponseData.builder()
                .message(ResponseMessages.SUCCESS)
                .status(HttpStatus.OK.value())
                .payload(categories)
                .build();
    }
}
