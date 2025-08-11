package com.jinanging.spring.libero.libero.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jinanging.spring.libero.libero.user.domain.OrderInfo;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {

    // 특정 사용자의 모든 주문 조회
    List<OrderInfo> findAllByUserId(long userId);
}
