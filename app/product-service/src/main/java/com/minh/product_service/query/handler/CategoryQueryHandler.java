package com.minh.product_service.query.handler;

import com.minh.common.constants.ErrorCode;
import com.minh.common.constants.ResponseMessages;
import com.minh.common.exception.ResourceNotFoundException;
import com.minh.common.message.MessageCommon;
import com.minh.common.response.ResponseData;
import com.minh.product_service.dto.CategoryDTO;
import com.minh.product_service.entity.Category;
import com.minh.product_service.query.queries.FindAllCategoriesQuery;
import com.minh.product_service.query.queries.FindCategoryByIdQuery;
import com.minh.product_service.query.queries.FindCategoryBySlug;
import com.minh.product_service.query.queries.SearchCategoriesByNameQuery;
import com.minh.product_service.query.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CategoryQueryHandler {
    private final CategoryService categoryService;
    private final MessageCommon messageCommon;

    @QueryHandler
    public ResponseData handle(FindCategoryByIdQuery findCategoryByIdQuery) throws ResourceNotFoundException {
        String categoryId = findCategoryByIdQuery.getCategoryId();
        return categoryService.getCategoryById(categoryId);
    }

    @QueryHandler
    public ResponseData handle(FindAllCategoriesQuery query) {
        return categoryService.findAllCategories(query);
    }

    @QueryHandler
    public ResponseData handle(FindCategoryBySlug query) {
        return categoryService.findCategoryBySlug(query);
    }

    @QueryHandler
    public ResponseData handle(SearchCategoriesByNameQuery query) {
        return categoryService.searchCategoriesByName(query);
    }
}
