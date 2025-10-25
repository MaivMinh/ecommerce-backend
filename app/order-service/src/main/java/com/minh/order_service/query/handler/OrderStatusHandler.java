package com.minh.order_service.query.handler;

import com.minh.common.constants.ResponseMessages;
import com.minh.common.events.CreatedOrderConfirmedEvent;
import com.minh.common.events.OrderCreatedRollbackedEvent;
import com.minh.order_service.payload.response.ResponseData;
import com.minh.order_service.query.queries.FindOverallStatusOfCreatingOrderQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class OrderStatusHandler {
    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;

    public OrderStatusHandler() {
    }

    @EventHandler
    public void on(CreatedOrderConfirmedEvent event) {
        log.info("Order with id {} has been created successfully.", event.getOrderId());

        Map<String,String> data = new HashMap<>();
        data.put("orderId", event.getOrderId());
        data.put("paymentId", event.getPaymentId());

        // emit chỉ tới subscriber có cùng orderId
        queryUpdateEmitter.emit(
                FindOverallStatusOfCreatingOrderQuery.class,
                query -> true,
                ResponseData.builder()
                        .status(HttpStatus.CREATED.value())
                        .message(ResponseMessages.SUCCESS)
                        .data(data)
                        .build()
        );
    }

    @EventHandler
    public void on(OrderCreatedRollbackedEvent event) {
        log.info("Order with id {} has been rolled back due to error: {}", event.getOrderId(), event.getErrorMsg());

        Map<String,String> data = new HashMap<>();
        data.put("orderId", event.getOrderId());
        data.put("errorMsg", event.getErrorMsg());

        queryUpdateEmitter.emit(
                FindOverallStatusOfCreatingOrderQuery.class,
                query -> true,
                ResponseData.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message("Đặt hàng thất bại. Vui lòng thử lại.")
                        .data(data)
                        .build()
        );
    }
}
