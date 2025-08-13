package com.minh.common.constants;

public class ErrorCode {
    public static final String INTERNAL_SERVER_ERROR = "internal.server.error";
    public static final String INVALID_PARAMS = "invalid.params";
    public static final String QUERY_EXECUTION_ERROR = "query.execution.error";

    public static class Category {
        public static final String NOT_FOUND = "category.not.found";
        public static final String SLUG_NOT_FOUND = "category.slug.not.found";
    }

    public static class Product {
        public static final String NOT_FOUND = "product.not.found";
        public static final String SLUG_NOT_FOUND = "product.slug.not.found";
    }
}
