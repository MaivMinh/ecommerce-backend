package com.minh.cart_service.service;

import com.minh.cart_service.DTO.CartItemDTO;
import com.minh.common.response.ResponseData;
import jakarta.validation.Valid;

public interface CartItemService {
    ResponseData createCartItem(CartItemDTO cartItemDTO);

    ResponseData updateCartItem(@Valid CartItemDTO cartItemDTO);

    ResponseData deleteCartItem(String id);

    ResponseData findCartItemOfUser();
}