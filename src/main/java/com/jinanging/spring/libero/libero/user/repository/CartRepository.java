package com.jinanging.spring.libero.libero.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jinanging.spring.libero.libero.user.domain.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 특정 사용자 장바구니 전체 조회
    List<Cart> findAllByUserId(long userId);

    // 특정 사용자, 특정 책 조회
    Optional<Cart> findByUserIdAndBookId(long userId, long bookId);

    // 특정 사용자 장바구니 삭제
    void deleteAllByUserId(long userId);
}
