package com.jinanging.spring.libero.libero.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinanging.spring.libero.libero.user.domain.OrderInfo;
import com.jinanging.spring.libero.libero.user.repository.OrderInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderInfoService {

    private final OrderInfoRepository orderInfoRepository;

    @Transactional
    public OrderInfo createOrderInfo(long userId, String address, String addressDetail, String addressNumber, String status) {
        return orderInfoRepository.save(
            OrderInfo.builder()
                .userId(userId)
                .address(address)
                .addressDetail(addressDetail)
                .addressNumber(addressNumber)
                .status(status)
                .build()
        );
    }

    public OrderInfo getOrderInfo(long orderInfoId) {
        return orderInfoRepository.findById(orderInfoId).orElse(null);
    }
}
