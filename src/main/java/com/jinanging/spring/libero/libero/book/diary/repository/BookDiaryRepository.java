package com.jinanging.spring.libero.libero.book.diary.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jinanging.spring.libero.libero.book.diary.domain.Diary;

public interface BookDiaryRepository extends JpaRepository<Diary, Long> {

    // 로그인한 사용자의 모든 일기 조회 (최신순)
    List<Diary> findAllByUserIdOrderByReadDateDesc(Long userId);

    // 특정 사용자 + 특정 책 제목 기준으로 일기 1개 가져오기 (중복 방지용 등)
    Diary findByUserIdAndBookTitle(Long userId, String bookTitle);
}
