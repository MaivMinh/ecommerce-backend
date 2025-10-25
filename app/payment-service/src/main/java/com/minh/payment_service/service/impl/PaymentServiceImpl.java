package com.minh.payment_service.service.impl;

import com.minh.common.constants.ResponseMessages;
import com.minh.payment_service.query.entity.Payment;
import com.minh.payment_service.repository.PaymentRepository;
import com.minh.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
}
