package com.minh.order_service.query.repository;

import com.minh.order_service.query.controller.SearchOrdersRequest;
import com.minh.order_service.query.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "SELECT od.id " +
            "FROM orders od " +
            "WHERE (COALESCE(:#{#request.orderId}, NULL) IS NULL OR od.id = :#{#request.orderId}) " +
            "AND (COALESCE(:#{#request.username}, NULL) IS NULL OR od.username = :#{#request.username}) " +
            "AND (COALESCE(:#{#request.status}, NULL) IS NULL OR od.status = :#{#request.status}) " +
            "ORDER BY od.id DESC " +
            "LIMIT :#{#pageable.pageSize} OFFSET :#{#pageable.offset}",
            countQuery = "SELECT COUNT(*) FROM orders od " +
                    "WHERE (COALESCE(:#{#request.orderId}, NULL) IS NULL OR od.id = :#{#request.orderId}) " +
                    "AND (COALESCE(:#{#request.username}, NULL) IS NULL OR od.username = :#{#request.username}) " +
                    "AND (COALESCE(:#{#request.status}, NULL) IS NULL OR od.status = :#{#request.status})",
            nativeQuery = true)
    Page<String> searchOrderIds(@Param("request") SearchOrdersRequest request, Pageable pageable);
}
