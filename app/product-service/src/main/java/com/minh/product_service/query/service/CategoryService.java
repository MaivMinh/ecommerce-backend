package com.minh.product_service.query.service;

import com.minh.common.exception.ResourceNotFoundException;
import com.minh.product_service.dto.CategoryDTO;
import com.minh.product_service.query.queries.FindAllCategoriesQuery;

import java.util.List;

public interface CategoryService {
    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category with the specified ID
     */
    CategoryDTO getCategoryById(String categoryId) throws ResourceNotFoundException;

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    List<CategoryDTO> findAllCategories(FindAllCategoriesQuery query);
}
