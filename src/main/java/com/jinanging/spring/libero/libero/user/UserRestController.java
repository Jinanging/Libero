package com.jinanging.spring.libero.libero.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jinanging.spring.libero.libero.api.ApiResponse;
import com.jinanging.spring.libero.libero.api.ResponseCode;
import com.jinanging.spring.libero.libero.user.service.UserService;

@RestController
@RequestMapping("libero/user")
public class UserRestController {
	
	 private final UserService userService;
	
	 public UserRestController(UserService userService) {
	        this.userService = userService;
	    }
	
	
	
	@PostMapping("/join")
	public ApiResponse<?> joinUser(
			@RequestParam String loginId
			,@RequestParam String password
			,@RequestParam String name
			,@RequestParam String address
			,@RequestParam String detailAddress
			,@RequestParam String addressNumber
			) {
		
		boolean result = userService.joinUser(loginId, password, name, address, detailAddress, addressNumber);

        if(result) {
            return ApiResponse.success(null); 
        } else {
            return ApiResponse.fail(ResponseCode.FAIL); 
        }
		
		
		
		
		
		
		
	}
		
		
		

	
	

}
