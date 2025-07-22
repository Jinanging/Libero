package com.jinanging.spring.libero.libero.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jinanging.spring.libero.libero.api.ApiResponse;
import com.jinanging.spring.libero.libero.api.ResponseCode;
import com.jinanging.spring.libero.libero.user.service.AuthService;
import com.jinanging.spring.libero.libero.user.service.UserService;

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
	         @RequestParam String password) {
		 //api는 그저 연결할 뿐이니까 비교 하는 추가 기능은 서비스에서 하는게!
	     return authService.login(loginId, password);
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
