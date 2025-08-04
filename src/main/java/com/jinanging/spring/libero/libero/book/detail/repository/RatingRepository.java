package com.jinanging.spring.libero.libero.book.detail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jinanging.spring.libero.libero.book.detail.domain.RatingComment;

public interface RatingRepository extends JpaRepository<RatingComment, Long> {
	
	List<RatingComment> findByBookIdOrderByCreatedAtDesc(Long bookId);

    List<RatingComment> findByUserId(Long userId);

    boolean existsByUserIdAndBookId(Long userId, Long bookId);

}
