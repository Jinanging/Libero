package com.jinanging.spring.libero.libero.book.detail.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinanging.spring.libero.libero.book.detail.domain.RatingComment;
import com.jinanging.spring.libero.libero.book.detail.repository.RatingRepository;
import com.jinanging.spring.libero.libero.user.domain.User;
import com.jinanging.spring.libero.libero.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingCommentService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    // 리뷰 등록
    public RatingComment save(Long bookId, Long userId, double rating, String comment) {
        RatingComment review = RatingComment.builder()
                .bookId(bookId)
                .userId(userId)
                .rating(rating)
                .comment(comment)
                .build();
        return ratingRepository.save(review);
    }

    // 리뷰 삭제
    public void delete(Long commentId, Long userId) {
        RatingComment comment = ratingRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
        if (comment.getUserId() != userId) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        ratingRepository.delete(comment);
    }

    // 리뷰 수정 (내부 메서드로 필드 변경)
    @Transactional
    public RatingComment update(Long commentId, Long userId, double rating, String commentText) {
        RatingComment comment = ratingRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("리뷰가 존재하지 않습니다."));
        if (comment.getUserId() != userId) {
            throw new SecurityException("수정 권한이 없습니다.");
        }
        comment.update(rating, commentText);
        return comment;
    }

    // 리뷰 조회 (내 리뷰 우선, userName 포함)
    public List<Map<String, Object>> getReviewListWithUserName(Long bookId, Long currentUserId) {
        List<RatingComment> all = ratingRepository.findByBookIdOrderByCreatedAtDesc(bookId);

        return all.stream()
                .sorted((a, b) -> {
                    if (a.getUserId() == currentUserId) return -1;
                    if (b.getUserId() == currentUserId) return 1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .map(comment -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", comment.getId());
                    map.put("bookId", comment.getBookId());
                    map.put("userId", comment.getUserId());
                    map.put("rating", comment.getRating());
                    map.put("comment", comment.getComment());
                    map.put("createdAt", comment.getCreatedAt());
                    map.put("editable", comment.getUserId() == currentUserId);
                    map.put("userName", getUserName(comment.getUserId()));
                    return map;
                })
                .collect(Collectors.toList());
    }

    // 유저가 이미 리뷰 작성했는지 체크
    public boolean hasUserReviewed(Long bookId, Long userId) {
        return ratingRepository.existsByUserIdAndBookId(userId, bookId);
    }

    // 유저 이름 조회
    private String getUserName(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        return userOpt.map(User::getName).orElse("탈퇴한 사용자");
    }
    
    public List<RatingComment> findByBookId(Long bookId) {
        return ratingRepository.findByBookIdOrderByCreatedAtDesc(bookId);
    }
}
