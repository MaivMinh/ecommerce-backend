package com.minh.review_service.controller;

import com.minh.common.response.ResponseData;
import com.minh.review_service.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping(value = "/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping(value = "/{productId}")
    public ResponseEntity<ResponseData> findReviewsByProductId(@PathVariable(name = "productId") String productId) {
        // Logic to find reviews by product ID
        ResponseData response = reviewService.findReviewsByProductId(productId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
