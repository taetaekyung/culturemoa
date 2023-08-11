package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

	//로그인 화면
	@GetMapping("/login/login")
	public void login() {
		
	}
	//아이디 찾기
	@GetMapping("/login/findid")
	public void findid() {
		
	}
	//아이디 찾기 결과
	@GetMapping("/login/findidcomplete")
	public void findidcomplete() {
		
	}
	//비밀번호 찾기
	@GetMapping("/login/findpassword")
	public void findpassword() {
		
	}
	//비밀번호 재설정
	@GetMapping("/login/passwordreset")
	public void passwordreset() {
		
	}
	//비밀번호 재설정 결과
	@GetMapping("/login/passwordresetcomplete")
	public void passwordresetcomplete() {
		
	}
	
}
