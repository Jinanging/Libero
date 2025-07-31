package com.jinanging.spring.libero.libero.book;

import org.springframework.stereotype.Controller;  // @Controller로 바꿔야 함
import org.springframework.web.bind.annotation.RequestMapping;

import com.jinanging.spring.libero.libero.book.aladin.Service.AladinService;

@Controller
@RequestMapping("/libero/book")
public class BookRestController {

    private final AladinService aladinService;

    public BookRestController(AladinService aladinService) {
        this.aladinService = aladinService;
    }

}
