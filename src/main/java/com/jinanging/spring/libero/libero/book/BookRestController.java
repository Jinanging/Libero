package com.jinanging.spring.libero.libero.book;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;  // @Controller로 바꿔야 함
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jinanging.spring.libero.libero.book.aladin.Service.AladinService;

@Controller
@RequestMapping("/libero/book")
public class BookRestController {

    private final AladinService aladinService;

    public BookRestController(AladinService aladinService) {
        this.aladinService = aladinService;
    }

    @GetMapping("/list")
    public String showBookList(@RequestParam(defaultValue = "100") int categoryId, Model model) {
        List<Map<String, Object>> books = aladinService.getBooksByCategory(categoryId);
        model.addAttribute("books", books);
        return "book/list";
    }
}
