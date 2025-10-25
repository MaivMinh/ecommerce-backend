package com.minh.side_service.service;

import com.minh.common.response.ResponseData;
import com.minh.side_service.DTO.AddressDto;
import com.minh.side_service.entity.Address;

public interface AddressService {
    ResponseData createAddress(AddressDto addressDto);

    ResponseData updateAddress(AddressDto addressDto);

    ResponseData deleteAddress(String id);

    ResponseData getAddressesByUsername();

    ResponseData setDefaultAddress(String id);

    AddressDto getAddressById(String shippingAddressId);
}
