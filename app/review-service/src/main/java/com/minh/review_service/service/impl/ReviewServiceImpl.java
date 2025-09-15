package com.minh.review_service.service.impl;

import com.minh.common.constants.ResponseMessages;
import com.minh.common.response.ResponseData;
import com.minh.review_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    @Override
    public ResponseData findReviewsByProductId(String productId) {
        return ResponseData.builder()
                .status(200)
                .message(ResponseMessages.SUCCESS)
                .data(null) // Replace with actual data retrieval logic
                .build();
    }
}
