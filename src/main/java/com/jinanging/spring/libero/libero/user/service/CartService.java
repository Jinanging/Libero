package com.jinanging.spring.libero.libero.user.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinanging.spring.libero.libero.user.domain.Cart;
import com.jinanging.spring.libero.libero.user.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    // 1️⃣ 장바구니 목록 조회
    public List<Cart> getCartList(long userId) {
        return cartRepository.findAllByUserId(userId);
    }

    // 2️⃣ CartId로 UserId 조회 (컨트롤러에서 리다이렉트용)
    public long getUserIdByCartId(long cartId) {
        return cartRepository.findById(cartId)
                .map(Cart::getUserId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found: " + cartId));
    }

    // 3️⃣ 수량 증가
    @Transactional
    public void increaseCount(long cartId) {
        cartRepository.findById(cartId).ifPresent(cart -> {
            cart.setCount(cart.getCount() + 1);
            cartRepository.save(cart);
        });
    }

    // 4️⃣ 수량 감소
    @Transactional
    public void decreaseCount(long cartId) {
        cartRepository.findById(cartId).ifPresent(cart -> {
            if (cart.getCount() > 1) {
                cart.setCount(cart.getCount() - 1);
                cartRepository.save(cart);
            }
        });
    }

    // 5️⃣ 장바구니 개별 삭제
    @Transactional
    public void deleteCart(long cartId) {
        cartRepository.deleteById(cartId);
    }

    // 6️⃣ 장바구니 전체 삭제 / 주문 후 호출 가능
    @Transactional
    public void clearCartByUserId(long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    // 7️⃣ 장바구니에 책 추가
    @Transactional
    public Cart addToCart(long userId, long bookId, int count) {
        return cartRepository.findByUserIdAndBookId(userId, bookId)
                .map(cart -> {
                    cart.setCount(cart.getCount() + count);
                    return cartRepository.save(cart);
                })
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .userId(userId)
                                .bookId(bookId)
                                .count(count)
                                .build()
                ));
    }

    // 8️⃣ 장바구니 전체 주문 (주문 후 장바구니 비우기)
    @Transactional
    public void orderAll(long userId) {
        // 실제 주문 로직은 OrderService와 연동 가능
        // 예: orderService.createOrdersFromCart(orderInfo, userId);
        
        // 주문 후 장바구니 비우기
        clearCartByUserId(userId);
    }
}
