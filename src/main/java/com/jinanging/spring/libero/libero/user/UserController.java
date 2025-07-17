package com.jinanging.spring.libero.libero.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("libero/user")
public class UserController {
	
	@GetMapping("/join-view")
	public String joinview(){
		
		return "user/join";
		
	}
	
	@GetMapping("/login-view")
	public String loginview() {
		
		return "user/login";
	}
	
	

}
