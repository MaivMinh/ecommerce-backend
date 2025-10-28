package com.minh.support_service.service;

import support_service.GetShippingAddressRequest;
import support_service.GetShippingAddressResponse;

public interface SupportService {

    GetShippingAddressResponse getShippingAddress(GetShippingAddressRequest request);
}
