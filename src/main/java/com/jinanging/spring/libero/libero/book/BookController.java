package com.jinanging.spring.libero.libero.book;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinanging.spring.libero.libero.book.aladin.Service.AladinService;
import com.jinanging.spring.libero.libero.jwt.JwtProvider;

@Controller
@RequestMapping("/libero/book")
public class BookController {

    private final AladinService aladinService;
    private final JwtProvider jwtProvider;

    public BookController(AladinService aladinService, JwtProvider jwtProvider) {
        this.aladinService = aladinService;
        this.jwtProvider = jwtProvider;
    }
    
    @GetMapping("/search-view")
    public String showSearchList(@RequestParam String keyword,
                                 Model model,
                                 HttpServletRequest request) {

        // 1. 알라딘 API를 통해 키워드 기반 도서 검색
        List<Map<String, Object>> books = aladinService.getBooksByKeyword(keyword);

        // 2. 검색어와 결과를 모델에 담음
        model.addAttribute("books", books);
        model.addAttribute("keyword", keyword);

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
                               Model model,
                               HttpServletRequest request) {

        // 카테고리별 책 리스트 조회
        List<Map<String, Object>> books = aladinService.getBooksByCategory(categoryId);
        model.addAttribute("books", books);

        // 쿠키에서 Authorization 토큰 꺼내기
        String token = null;
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "Authorization".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        // 토큰이 존재하고 유효하면 토큰에서 userName 꺼내서 뷰에 전달
        if (token != null && jwtProvider.validateToken(token)) {
            String userName = jwtProvider.getUserName(token);
            model.addAttribute("userName", userName);
        } else {
            model.addAttribute("userName", null);
        }

        return "book/list";  
    }
}
