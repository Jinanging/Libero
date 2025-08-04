package com.jinanging.spring.libero.libero.book.detail;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jinanging.spring.libero.libero.book.detail.domain.RatingComment;
import com.jinanging.spring.libero.libero.book.detail.service.RatingCommentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/libero/book")
public class DetailController {

    private final RatingCommentService ratingCommentService;

    // 댓글 + 평점 저장
    @PostMapping("/{bookId}/review")
    public ResponseEntity<?> saveReview(
            @PathVariable Long bookId,
            @RequestBody Map<String, Object> payload,
            HttpSession session) {

        // 로그인 유저 아이디 받아오기 (예: 세션에서)
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "로그인이 필요합니다."));
        }

        Double rating = null;
        String comment = null;
        try {
            rating = Double.parseDouble(payload.get("rating").toString());
            comment = payload.get("comment").toString();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "잘못된 요청 데이터입니다."));
        }

        RatingComment saved = ratingCommentService.save(bookId, userId, rating, comment);
        return ResponseEntity.ok(Map.of("result", "success", "id", saved.getId()));
    }

    // 특정 책 리뷰 목록 가져오기
    @GetMapping("/{bookId}/review")
    public ResponseEntity<?> getReviews(@PathVariable Long bookId) {
        List<RatingComment> reviews = ratingCommentService.findByBookId(bookId);
        return ResponseEntity.ok(reviews);
    }
}
