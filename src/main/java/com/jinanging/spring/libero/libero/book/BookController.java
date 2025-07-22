package com.jinanging.spring.libero.libero.book;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/libero/book")
@Controller
public class BookController {
	
	@GetMapping("/list-view")
	public String bookList() {
		
		return "book/list";
		
	}

}
