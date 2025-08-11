package com.jinanging.spring.libero.libero.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jinanging.spring.libero.libero.user.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 특정 주문 정보(OrderInfo)에 해당하는 주문 조회
    List<Order> findAllByOrderInfoId(long orderInfoId);
}
