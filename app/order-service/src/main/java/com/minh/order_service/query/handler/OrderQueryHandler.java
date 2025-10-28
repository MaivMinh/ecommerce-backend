package com.minh.order_service.query.handler;

import com.minh.common.utils.AppUtils;
import com.minh.order_service.payload.request.SearchOrdersForUserRequest;
import com.minh.order_service.payload.response.OrderDetailRes;
import com.minh.order_service.payload.response.ResponseData;
import com.minh.order_service.query.controller.SearchOrdersRequest;
import com.minh.order_service.query.queries.FindOverallStatusOfCreatingOrderQuery;
import com.minh.order_service.query.queries.GetOrderDetailQuery;
import com.minh.order_service.query.queries.SearchOrdersForUserQuery;
import com.minh.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderQueryHandler {
    private final OrderService orderService;

    @QueryHandler
    public ResponseData handle(FindOverallStatusOfCreatingOrderQuery query) {
        return orderService.findOverallStatusOfCreatingOrder(query);
    }

    @QueryHandler
    public OrderDetailRes handle(GetOrderDetailQuery query) {
        return orderService.getOrderDetail(query);
    }

    @QueryHandler
    public ResponseData handle(SearchOrdersRequest request) {
        return orderService.searchOrders(request);
    }

    @QueryHandler
    public ResponseData handle(SearchOrdersForUserQuery query) {
        return orderService.searchOrdersForUser(query);
    }
}