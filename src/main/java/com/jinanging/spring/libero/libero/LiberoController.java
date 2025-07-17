package com.jinanging.spring.libero.libero;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/libero")
public class LiberoController {
	
	@GetMapping("/hook")
	public String hookView() {
		return "main/hook";
	}
	
	@GetMapping("/category")
	public String categoryView() {
		return "main/category";
	}
	
	

}
