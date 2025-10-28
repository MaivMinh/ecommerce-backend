package com.minh.support_service.service.impl;

import com.minh.support_service.DTO.AddressDto;
import com.minh.support_service.service.AddressService;
import com.minh.support_service.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import support_service.GetShippingAddressRequest;
import support_service.GetShippingAddressResponse;

@Service
@RequiredArgsConstructor
public class SupportServiceImpl implements SupportService {
    private final AddressService addressService;

    @Override
    public GetShippingAddressResponse getShippingAddress(GetShippingAddressRequest request) {
        if (!StringUtils.hasText(request.getShippingAddressId())) {
            return GetShippingAddressResponse.newBuilder()
                    .setStatus(400)
                    .setMessage("Thiếu thông tin địa chỉ giao hàng.")
                    .build();
        }

        String addressId = request.getShippingAddressId();
        AddressDto address = addressService.findById(addressId);
        if (address == null) {
            return GetShippingAddressResponse.newBuilder()
                    .setStatus(404)
                    .setMessage("Địa chỉ giao hàng không tồn tại.")
                    .build();
        }
        return GetShippingAddressResponse.newBuilder()
                .setStatus(200)
                .setMessage("Lấy địa chỉ giao hàng thành công.")
                .setId(address.getId())
                .setFullName(address.getFullName())
                .setAddress(address.getAddress())
                .setPhone(address.getPhone())
                .build();
    }
}
