package com.ureca.miniproject.user.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//login.html 에서 window.onload event 에서 이 token 값을 요쳥
@RestController  
public class CsrfController {
	@GetMapping("/csrf-token")
	public CsrfToken csrf(CsrfToken token) {
		return token; //spring이 token을 내려주면 이것이 그것을 return하는 .-> front에서 이걸 받아서 이ㅣ제 window.onload로 보내줘야 로그인 가능
	}
}
