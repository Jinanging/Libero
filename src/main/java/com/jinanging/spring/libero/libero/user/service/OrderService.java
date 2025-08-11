package com.jinanging.spring.libero.libero.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinanging.spring.libero.libero.user.domain.Cart;
import com.jinanging.spring.libero.libero.user.domain.Order;
import com.jinanging.spring.libero.libero.user.domain.OrderInfo;
import com.jinanging.spring.libero.libero.user.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    // 장바구니의 모든 항목으로 주문 생성
    @Transactional
    public void createOrdersFromCart(OrderInfo orderInfo, long userId) {
        List<Cart> cartList = cartService.getCartList(userId); // CartService 메소드명과 맞춤
        for (Cart cart : cartList) {
            orderRepository.save(
                Order.builder()
                    .bookId(cart.getBookId())
                    .orderInfoId(orderInfo.getId())
                    .build()
            );
        }
        cartService.clearCartByUserId(userId); // Cart 비우기
    }

    // 특정 주문 정보에 속한 모든 주문 조회
    public List<Order> getOrdersByOrderInfoId(long orderInfoId) {
        return orderRepository.findAllByOrderInfoId(orderInfoId);
    }
}
