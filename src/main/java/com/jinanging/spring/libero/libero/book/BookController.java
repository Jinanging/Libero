package com.jinanging.spring.libero.libero.book;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinanging.spring.libero.libero.book.aladin.Service.AladinService;
import com.jinanging.spring.libero.libero.jwt.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/libero/book")
public class BookController {

    private final AladinService aladinService;
    private final JwtProvider jwtProvider;

    public BookController(AladinService aladinService, JwtProvider jwtProvider) {
        this.aladinService = aladinService;
        this.jwtProvider = jwtProvider;
    }
    
    @GetMapping("/diary-view")
    public String diary() {
    	
    	return "book/diary";
    }
    // 책 디테일 뷰 가져오기
    @GetMapping("/detail-view")
    public String bookDetail(@RequestParam("itemId") long itemId, Model model, HttpServletRequest request) {
        // 1. 책 정보 조회
        List<Map<String, Object>> books = aladinService.getBooksById(itemId);

        if (books != null && !books.isEmpty() && books.get(0) != null) {
            model.addAttribute("book", books.get(0));
        } else {
            model.addAttribute("book", new HashMap<>());
            model.addAttribute("error", "도서 정보를 불러올 수 없습니다.");
        }

        // 2. 로그인 정보 쿠키에서 꺼내서 userName 모델에 넣기
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "Authorization".equals(cookie.getName()))
                    .findFirst()
                    .map(jakarta.servlet.http.Cookie::getValue)
                    .orElse(null);
        }

        if (token != null && jwtProvider.validateToken(token)) {
            String userName = jwtProvider.getUserName(token);
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", null);
        }

        // 3. 뷰 이름 반환
        return "book/detail";  // /templates/book/detail.html
    }
    @GetMapping("/search-view")
    public String showSearchList(@RequestParam String keyword,
                                 @RequestParam(defaultValue = "1") int page,
                                 Model model,
                                 HttpServletRequest request) {

        // 1. 알라딘 API를 통해 키워드 기반 도서 검색 (페이지 포함)
        List<Map<String, Object>> books = aladinService.getBooksByKeyword(keyword, page);

        // 2. 검색어, 결과, 페이지 정보 모델에 담기
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 10);  // 최대 3페이지 제한

        // 3. 로그인한 사용자 이름이 있으면 전달
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "Authorization".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (token != null && jwtProvider.validateToken(token)) {
            String userName = jwtProvider.getUserName(token);
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", null);
        }

        return "book/search";	
    }

    @GetMapping("/list-view")
    public String showBookList(@RequestParam(defaultValue = "100") int categoryId,
                               @RequestParam(defaultValue = "1") int page,
                               Model model,
                               HttpServletRequest request) {

        // 1. 카테고리별 책 리스트 조회 (페이지 포함)
        List<Map<String, Object>> books = aladinService.getBooksByCategory(categoryId, page);
        model.addAttribute("books", books);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", 10); 
        // 최대 3페이지 제한

        // 2. 로그인 사용자 이름 전달
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "Authorization".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        if (token != null && jwtProvider.validateToken(token)) {
            String userName = jwtProvider.getUserName(token);
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", null);
        }

        return "book/list";  
    }
}
