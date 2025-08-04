package com.jinanging.spring.libero.libero.user;

import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jinanging.spring.libero.libero.api.ApiResponse;
import com.jinanging.spring.libero.libero.api.ResponseCode;
import com.jinanging.spring.libero.libero.user.service.AuthService;
import com.jinanging.spring.libero.libero.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("libero/user")
public class UserRestController {
	
	 private final UserService userService;
	 private final AuthService authService;
	
	 public UserRestController(UserService userService, AuthService authService) {
	        this.userService = userService;
	        this.authService = authService;
	    }
	
	 
	 @PostMapping("/login")
	 public ApiResponse<String> userLogin(
	     @RequestParam String loginId,
	     @RequestParam String password,
	     HttpServletResponse response 
	 ) {
	     ApiResponse<String> result = authService.login(loginId, password);

	     if ("success".equals(result.getResult())) {
	         String token = result.getData();

	         ResponseCookie cookie = ResponseCookie.from("Authorization", token)
	             .httpOnly(true)
	             .secure(false) 
	             .path("/")
	             .maxAge(60 * 60 * 24) 
	             .sameSite("Lax")  
	             .build();

	         response.addHeader("Set-Cookie", cookie.toString());
	     }

	     return result;
	 }
	 
	 @PostMapping("/logout")
	 @ResponseBody
	 public  ApiResponse<Void> logoutAjax(HttpServletResponse response) {
	     Cookie cookie = new Cookie("Authorization", null);
	     cookie.setMaxAge(0);
	     cookie.setPath("/");
	     response.addCookie(cookie);
	     return ApiResponse.success(null);
	 }

	
	@GetMapping("/isDuplicate")
	public ApiResponse<?> dupicatedIdCheck(@RequestParam String loginId){
		
		boolean result = userService.duplicatedId(loginId);
		if(result) {
			return ApiResponse.fail(ResponseCode.DUPLICATE_ID);
		}
		else {
			return ApiResponse.success(null);
		}
	}
	
	@PostMapping("/join")
	public ApiResponse<?> joinUser(
			@RequestParam String loginId
			,@RequestParam String password
			,@RequestParam String name
			,@RequestParam String address
			,@RequestParam String addressDetail
			,@RequestParam String addressNumber
			) throws Exception {
		
		boolean result = userService.joinUser(loginId, password, name, address, addressDetail, addressNumber);

        if(result) {
            return ApiResponse.success(null); 
        } else {
            return ApiResponse.fail(ResponseCode.FAIL); 
        }
		
		
		
		
		
		
		
	}
		
		
		

	
	

}
