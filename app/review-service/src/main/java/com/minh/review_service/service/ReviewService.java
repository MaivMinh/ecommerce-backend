package com.minh.review_service.service;

import com.minh.common.response.ResponseData;

public interface ReviewService {

    ResponseData findReviewsByProductId(String productId);
}
