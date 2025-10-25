package com.minh.order_service.command.controller;

import com.minh.common.response.ResponseData;
import com.minh.common.utils.AppUtils;
import com.minh.order_service.command.commands.CreateOrderCommand;
import com.minh.order_service.payload.request.CreateOrderRequest;
import com.minh.order_service.query.queries.FindOverallStatusOfCreatingOrderQuery;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RequestMapping(value = "/api/orders")
@RequiredArgsConstructor
@Validated
@RestController
public class OrderCommandController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @PostMapping(value = "")
    public ResponseEntity<ResponseData> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderCommand command = CreateOrderCommand.builder()
                .orderId(AppUtils.generateUUIDv7())
                .username(AppUtils.getUsername())
                .shippingAddressId(request.getShippingAddressId())
                .currency(request.getCurrency())
                .discount(request.getDiscount())
                .total(request.getTotal())
                .subTotal(request.getSubTotal())
                .note(request.getNote())
                .paymentMethodId(request.getPaymentMethodId())
                .orderItemDtos(request.getOrderItemDtos())
                .promotionId(request.getPromotionId())
                .orderItemDtos(request.getOrderItemDtos())
                .build();

        try (SubscriptionQueryResult<ResponseData, ResponseData> queryResult = queryGateway.subscriptionQuery(new FindOverallStatusOfCreatingOrderQuery(command.getOrderId()), ResponseData.class, ResponseData.class);) {
            commandGateway.send(command, new CommandCallback<>() {
                @Override
                public void onResult(@Nonnull CommandMessage<? extends CreateOrderCommand> commandMessage, @Nonnull CommandResultMessage<?> commandResultMessage) {
                    if (commandResultMessage.isExceptional()) {
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .body(new ResponseData(500, "Không thể tạo mới đơn hàng: " + commandResultMessage.exceptionResult().getMessage(), null));
                    }
                }
            });
            // Store the first update to avoid multiple blocking calls
            ResponseData responseData = queryResult.updates().blockFirst(Duration.ofMinutes(15));
            if (responseData == null) {
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                        .body(new ResponseData(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Mất quá lâu để tạo mới đơn hàng", null));
            }
            return ResponseEntity.status(responseData.getStatus()).body(responseData);
        }
    }
}
