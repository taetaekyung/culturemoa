package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

import jakarta.servlet.http.HttpSession;
import lombok.Setter;

@Controller
@Setter
public class LoginController {

	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	//로그인 화면
	@GetMapping("/login/login")
	public void loginform() {
		
	}
	
	//로그인 화면
	@PostMapping("/login/login")
	public ModelAndView login(HttpSession session, String id, String password) {
		ModelAndView mav = new ModelAndView("redirect:/mainPage");
		System.out.println(password);

		if(memberdao_jpa.findByUserId(id) == null || memberdao_jpa.findByUserId(id).equals("")) {
			mav.addObject("msg", "존재하지 않는 아이디입니다.");
			mav.setViewName("/login/login");
		}
		else {
			String pwd = ((Member)memberdao_jpa.findById(id).get()).getPwd();
			if(!pwd.equals(password)) {
				mav.addObject("msg", "비밀번호가 일치하지 않습니다.");
				mav.setViewName("/login/login");
			}
			else {
				session.setAttribute("m", (Member)memberdao_jpa.findById(id).get());
			}
		}
		return mav;
	}
	
	//로그아웃 기능
	@GetMapping("/login/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // 로그아웃 시 세션파기
		return "redirect:/mainPage";
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
