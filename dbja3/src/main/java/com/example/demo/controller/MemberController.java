package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.MemberDAO_jpa;
import com.example.demo.entity.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberDAO_jpa memberdao_jpa;
	
	@GetMapping("/member/mypage")
	public void mypage(HttpSession session) {
	}
	
	@GetMapping("/member/editmypage")
	public void editmypageform(Model model, Member m) {
		model.addAttribute("m", m);
	}
	
	@GetMapping("/member/editpic")
	public void editpic(HttpSession session) {
	}
	
	
	@GetMapping("/member/changepwd")
	public void changepwd() {
	}
	

	
	@PostMapping("/member/editmypage")
	public ModelAndView editmypage(HttpSession session, String nickname, String email) {
		ModelAndView mav = new ModelAndView("redirect:/member/mypage");
		Member m = (Member)session.getAttribute("m");
		if(nickname != null && !nickname.equals("")) {
			m.setNickname(nickname);
		}
		if(email != null && !email.equals("")) {
			m.setEmail(email);
		}
		memberdao_jpa.save(m);
		return mav;
	}
}
