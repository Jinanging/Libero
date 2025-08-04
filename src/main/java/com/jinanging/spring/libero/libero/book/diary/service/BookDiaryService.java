package com.jinanging.spring.libero.libero.book.diary.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jinanging.spring.libero.libero.book.diary.domain.Diary;
import com.jinanging.spring.libero.libero.book.diary.repository.BookDiaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookDiaryService {

    private final BookDiaryRepository diaryRepository;

    // 1. 다이어리 저장 (작성 or 수정)
    @Transactional
    public Diary saveOrUpdateDiary(Diary diary) {
        Diary existing = diaryRepository.findByUserIdAndBookTitle(diary.getUserId(), diary.getBookTitle());
        
        if (existing != null) {
            Diary updated = Diary.builder()
                    .id(existing.getId())
                    .userId(existing.getUserId())
                    .bookTitle(existing.getBookTitle())
                    .contents(diary.getContents())
                    .readDate(diary.getReadDate())
                    .build();
            return diaryRepository.save(updated);
        } else {
            return diaryRepository.save(diary);
        }
    }

    // 2. 특정 사용자 다이어리 전체 조회 (최신순)
    public List<Diary> getDiariesByUser(Long userId) {
        return diaryRepository.findAllByUserIdOrderByReadDateDesc(userId);
    }

    // 3. 특정 유저 + 책 제목 기준으로 단건 조회
    public Diary getDiaryByUserAndBook(Long userId, String bookTitle) {
        return diaryRepository.findByUserIdAndBookTitle(userId, bookTitle);
    }

    // 4. 다이어리 삭제
    @Transactional
    public void deleteDiary(Long diaryId, Long userId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new IllegalArgumentException("다이어리를 찾을 수 없습니다."));
        if (diary.getUserId() != userId.longValue()) {
            throw new SecurityException("삭제 권한이 없습니다.");
        }
        diaryRepository.delete(diary);
    }
}
