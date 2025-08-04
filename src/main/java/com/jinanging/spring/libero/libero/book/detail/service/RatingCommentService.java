package com.jinanging.spring.libero.libero.book.detail.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.jinanging.spring.libero.libero.book.detail.domain.RatingComment;
import com.jinanging.spring.libero.libero.book.detail.repository.RatingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingCommentService {

    private final RatingRepository ratingRepository;

    public RatingComment save(Long bookId, Long userId, double rating, String comment) {
        RatingComment review = RatingComment.builder()
                .bookId(bookId)
                .userId(userId)
                .rating(rating)
                .comment(comment)
                .build();
        return ratingRepository.save(review);
    }

    public List<RatingComment> findByBookId(Long bookId) {
        return ratingRepository.findByBookIdOrderByCreatedAtDesc(bookId);
    }
}
