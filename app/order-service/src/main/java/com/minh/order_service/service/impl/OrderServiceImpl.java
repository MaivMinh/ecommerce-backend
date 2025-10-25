package com.minh.order_service.service.impl;

import com.minh.common.DTOs.OrderItemCreateDto;
import com.minh.common.constants.ErrorCode;
import com.minh.common.constants.ResponseMessages;
import com.minh.common.events.CreatedOrderConfirmedEvent;
import com.minh.common.events.OrderCreatedEvent;
import com.minh.common.events.OrderCreatedRollbackedEvent;
import com.minh.common.message.MessageCommon;
import com.minh.common.utils.AppUtils;
import com.minh.order_service.enums.OrderStatus;
import com.minh.order_service.enums.PaymentStatus;
import com.minh.order_service.payload.response.*;
import com.minh.order_service.query.controller.SearchOrdersRequest;
import com.minh.order_service.query.entity.Order;
import com.minh.order_service.query.entity.OrderItem;
import com.minh.order_service.query.queries.FindOverallStatusOfCreatingOrderQuery;
import com.minh.order_service.query.queries.GetOrderDetailQuery;
import com.minh.order_service.query.repository.OrderRepository;
import com.minh.order_service.service.OrderItemService;
import com.minh.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final MessageCommon messageCommon;
    private final OrderItemService orderItemService;

    @Override
    @Transactional
    public void createOrder(OrderCreatedEvent event) {
        log.info("Creating order with id: {}", event.getOrderId());
        Order order = new Order();
        modelMapper.map(event, order);
        order.setId(event.getOrderId());
        order.setStatus(OrderStatus.CREATED);
        orderRepository.save(order);

        /// Store order items in DB.
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCreateDto dto : event.getOrderItemDtos()) {
            OrderItem item = new OrderItem();
            modelMapper.map(dto, item);
            item.setOrderId(event.getOrderId());
            items.add(item);
            item.setId(AppUtils.generateUUIDv7());
        }
        orderItemService.saveAll(items);
    }

    @Override
    @Transactional
    public void rollbackOrderCreated(OrderCreatedRollbackedEvent event) {
        Order order = orderRepository.findById(event.getOrderId()).orElseThrow(
                () -> new RuntimeException(messageCommon.getMessage(ErrorCode.Order.NOT_FOUND, event.getOrderId()))
        );
        order.setStatus(OrderStatus.CANCELLED);

        /// Remove all order items related to this order.
        orderItemService.removeAllByOrderId(order.getId());
    }

    @Override
    public void confirmCreatedOrder(CreatedOrderConfirmedEvent event) {
        Order saved = orderRepository.findById(event.getOrderId()).orElseThrow(
                () -> new RuntimeException(messageCommon.getMessage(ErrorCode.Order.NOT_FOUND, event.getOrderId()))
        );

        saved.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(saved);
    }

    public ResponseData findOverallStatusOfCreatingOrder(FindOverallStatusOfCreatingOrderQuery query) {
        Order order = orderRepository.findById(query.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + query.getOrderId()));
        return ResponseData.builder()
                .status(200)
                .message("Lấy thông tin đơn hàng thành công")
                .data(Collections.singletonMap("orderStatus", order.getStatus()))
                .build();
    }

    @Override
    public OrderDetailRes getOrderDetail(GetOrderDetailQuery query) {
//        try {
//            Order order = orderRepository.findById(query.getOrderId())
//                    .orElseThrow(() -> new RuntimeException(messageCommon.getMessage(ErrorCode.Order.NOT_FOUND, query.getOrderId())));
//
//            OrderDetailRes response = new OrderDetailRes();
//            response.setId(order.getId());
//            response.setCurrency(order.getCurrency());
//            response.setDiscount(order.getDiscount());
//            response.setNote(order.getNote());
//            response.setStatus(order.getStatus().name());
//            response.setSubTotal(order.getSubTotal());
//            response.setTotal(order.getTotal());
//            response.setItems(new ArrayList<>());
//
//            //Get Shipping Address.
//            GetShippingAddressRequest request = GetShippingAddressRequest.newBuilder()
//                    .setShippingAddressId(order.getShippingAddressId())
//                    .build();
//
//            GetShippingAddressResponse shippingAddressGrpcRes = sideServiceGrpcClient.getShippingAddress(request);
//
//            if (shippingAddressGrpcRes.getStatus() == HttpStatus.NOT_FOUND.value()) {
//                throw new RuntimeException(messageCommon.getMessage(ErrorCode.Address.NOT_FOUND, order.getShippingAddressId()));
//            }
//            if (shippingAddressGrpcRes.getStatus() != HttpStatus.OK.value()) {
//                throw new RuntimeException(shippingAddressGrpcRes.getMessage());
//            }
//
//            ShippingAddressRes shippingAddressRes = new ShippingAddressRes();
//            shippingAddressRes.setId(shippingAddressGrpcRes.getId());
//            shippingAddressRes.setAddress(shippingAddressGrpcRes.getAddress());
//            shippingAddressRes.setFullName(shippingAddressGrpcRes.getFullName());
//            shippingAddressRes.setPhone(shippingAddressGrpcRes.getPhone());
//            response.setShippingAddress(shippingAddressRes);
//
//            /// Get product variant for each item.
//            List<OrderItem> items = orderItemService.getAllByOrderId(order.getId());
//            /// Mapping id -> OrderItem.
//            Map<String, OrderItem> itemMap = items.stream()
//                    .collect(java.util.stream.Collectors.toMap(OrderItem::getId, item -> item));
//
//
//            List<OrderItemAndProductVariantId> ids = items.stream().map(item -> OrderItemAndProductVariantId.newBuilder()
//                    .setOrderItemId(item.getId())
//                    .setProductVariantId(item.getProductVariantId())
//                    .build()).toList();
//            FindProductVariantByListProductVariantIdRequest req = FindProductVariantByListProductVariantIdRequest.newBuilder()
//                    .addAllIds(ids)
//                    .build();
//
//            FindProductVariantByListProductVariantIdResponse res = productServiceGrpcClient.findProductVariantByListId(req);
//            if (res.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                throw new RuntimeException(messageCommon.getMessage(ErrorCode.INTERNAL_SERVER_ERROR));
//            }
//
//            List<ProductVariantRes> productVariantRes = res.getProductVariantsList();
//            List<OrderItemRes> orderItemResList = new ArrayList<>();
//            for (ProductVariantRes productVariant : productVariantRes) {
//                OrderItem orderItem = itemMap.get(productVariant.getOrderItemId());
//                OrderItemRes itemRes = new OrderItemRes();
//                itemRes.setId(orderItem.getId());
//                itemRes.setPrice(orderItem.getPrice());
//                itemRes.setQuantity(orderItem.getQuantity());
//                itemRes.setTotal(orderItem.getTotal());
//                itemRes.setProductVariant(com.minh.order_service.payload.response.ProductVariantRes.builder()
//                        .id(productVariant.getId())
//                        .colorHex(productVariant.getColorHex())
//                        .colorName(productVariant.getColorName())
//                        .cover(productVariant.getCover())
//                        .name(productVariant.getName())
//                        .originalPrice(productVariant.getOriginalPrice())
//                        .price(productVariant.getPrice())
//                        .size(productVariant.getSize())
//                        .slug(productVariant.getSlug())
//                        .build());
//                orderItemResList.add(itemRes);
//            }
//            response.setItems(orderItemResList);
//
//            /// Get Payment status of order.
//            GetPaymentStatusRequest paymentStatusRequest = GetPaymentStatusRequest.newBuilder()
//                    .setOrderId(order.getId())
//                    .build();
//            GetPaymentStatusResponse paymentStatusResponse = paymentServiceGrpcClient.getPaymentStatus(paymentStatusRequest);
//            if (paymentStatusResponse.getStatus() != HttpStatus.OK.value()) {
//                throw new RuntimeException(messageCommon.getMessage(paymentStatusResponse.getMessage()));
//            }
//            response.setPaymentStatus(PaymentStatus.valueOf(paymentStatusResponse.getPaymentStatus()));
//            return response;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        return null;
    }

    @Override
    public ResponseData searchOrders(SearchOrdersRequest request) {
        Pageable pageable = AppUtils.toPageable(request);
        Page<String> orderIds = orderRepository.searchOrderIds(request, pageable);

        List<OrderDetailRes> response = new ArrayList<>();
        for (String orderId : orderIds.getContent()) {
            GetOrderDetailQuery query = GetOrderDetailQuery.builder()
                    .orderId(orderId)
                    .build();
            OrderDetailRes orderDetail = getOrderDetail(query);
            response.add(orderDetail);
        }
        return ResponseData.builder()
                .status(200)
                .message(ResponseMessages.SUCCESS)
                .data(response)
                .build();
    }
}
