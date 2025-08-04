package com.jinanging.spring.libero.libero.book.diary;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jinanging.spring.libero.libero.book.diary.domain.Diary;
import com.jinanging.spring.libero.libero.book.diary.service.BookDiaryService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/libero/book/diary")
public class DiaryController {

    private final BookDiaryService diaryService;

    // 내 모든 일기 리스트 가져오기
    @GetMapping("/list")
    public ResponseEntity<?> getDiaryList(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<Diary> diaries = diaryService.getDiariesByUser(userId);
        return ResponseEntity.ok(diaries);
    }

    // 일기 작성 or 수정
    @PostMapping("/save")
    public ResponseEntity<?> saveDiary(@RequestBody Diary diary, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        // userId 세션값으로 덮어쓰기 + bookTitle 직접 입력 받음
        Diary newDiary = Diary.builder()
                .id(diary.getId()) // 수정시 id 존재, 새로 작성시 null
                .userId(userId)
                .bookTitle(diary.getBookTitle())  // 변경: bookId → bookTitle
                .contents(diary.getContents())
                .readDate(diary.getReadDate())
                .build();

        Diary saved = diaryService.saveOrUpdateDiary(newDiary);
        return ResponseEntity.ok(saved);
    }

    // 특정 일기 단건 조회 (책 제목으로 찾기)
    @GetMapping("/detail")
    public ResponseEntity<?> getDiaryByBook(@RequestParam String bookTitle, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        Diary diary = diaryService.getDiaryByUserAndBook(userId, bookTitle);
        if (diary == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일기를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(diary);
    }

    // 일기 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDiary(@PathVariable Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            diaryService.deleteDiary(id, userId);
            return ResponseEntity.ok("삭제되었습니다.");
        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(se.getMessage());
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(iae.getMessage());
        }
    }
}
