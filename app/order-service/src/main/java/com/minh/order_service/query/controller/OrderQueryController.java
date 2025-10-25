package com.minh.order_service.query.controller;

import com.minh.common.constants.ResponseMessages;
import com.minh.order_service.payload.response.OrderDetailRes;
import com.minh.order_service.payload.response.ResponseData;
import com.minh.order_service.query.queries.GetOrderDetailQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Validated
@RequiredArgsConstructor
public class OrderQueryController {
    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getOrderDetail(@PathVariable String id) {
        GetOrderDetailQuery query = GetOrderDetailQuery.builder().orderId(id).build();
        OrderDetailRes orderDetail = queryGateway.query(query, ResponseTypes.instanceOf(OrderDetailRes.class)).join();
        ResponseData response = ResponseData.builder()
                .status(200)
                .message(ResponseMessages.SUCCESS)
                .data(orderDetail)
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/all")
    public ResponseEntity<ResponseData> getOrders(@RequestBody SearchOrdersRequest request) {
        ResponseData response = queryGateway.query(request, ResponseTypes.instanceOf(ResponseData.class)).join();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
