package com.jinanging.spring.libero.libero.book.detail;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jinanging.spring.libero.libero.book.detail.service.RatingCommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/libero/book")
public class DetailController {

    private final RatingCommentService ratingCommentService;

    // 댓글 + 평점 저장 (유저가 이미 리뷰 있으면 400 에러)
    @PostMapping("/{bookId}/review")
    public ResponseEntity<?> saveReview(
            @PathVariable Long bookId,
            @RequestBody Map<String, Object> payload,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        // 유저가 이미 리뷰 작성했는지 체크
        if (ratingCommentService.hasUserReviewed(bookId, userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "이미 리뷰를 작성하셨습니다."));
        }

        Double rating;
        String comment;
        try {
            rating = Double.parseDouble(payload.get("rating").toString());
            comment = payload.get("comment").toString();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 요청 데이터입니다."));
        }

        var saved = ratingCommentService.save(bookId, userId, rating, comment);
        return ResponseEntity.ok(Map.of("result", "success", "id", saved.getId()));
    }

    // 특정 책 리뷰 목록 (내 리뷰가 위로, editable 포함 userName 포함)
    @GetMapping("/{bookId}/review")
    public ResponseEntity<?> getReviews(
            @PathVariable Long bookId,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        List<Map<String, Object>> reviews = ratingCommentService.getReviewListWithUserName(bookId, userId);
        return ResponseEntity.ok(reviews);
    }

    // 리뷰 수정
    @PutMapping("/review/{commentId}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long commentId,
            @RequestBody Map<String, Object> payload,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        Double rating;
        String comment;
        try {
            rating = Double.parseDouble(payload.get("rating").toString());
            comment = payload.get("comment").toString();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 요청 데이터입니다."));
        }

        try {
            var updated = ratingCommentService.update(commentId, userId, rating, comment);
            return ResponseEntity.ok(Map.of("result", "success", "id", updated.getId()));
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", se.getMessage()));
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", re.getMessage()));
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/review/{commentId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long commentId,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        try {
            ratingCommentService.delete(commentId, userId);
            return ResponseEntity.ok(Map.of("result", "success"));
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", se.getMessage()));
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", re.getMessage()));
        }
    }
}
